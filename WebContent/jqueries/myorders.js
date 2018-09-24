var loggedIn = false;
var artikalCNT = 1;
var totalSum = 0;

var usedTokens = 0;

var AllOrdersArray = [];

$(function(){
	
	$("#noteTextArea").hide();	

	//gettovanje korisnika
	$.ajax({
		type: 'GET',
		url: 'rest/users/currentUser',
		contentType: 'application/json',
		dataType:"json",
		complete: function(data)
		{
			
			var loggedUser = data.responseJSON;
			
			console.log(data.responseJSON);
			
			if(loggedUser != null)
			{
				loggedIn = true;
				setUpForAnLoggedInUser(loggedUser);
			}
			else //ako ostane nekako neulogovan na stranici
			{
				window.location.href = "index.html";
			}
		}  
		});
	
	
		//gettovanje lista narudzbina
		$.ajax({
			type: 'GET',
			url: 'rest/users/getBasketContent',
			success: function(data)
			{
				if(data.length == 0)
				{
					console.log("Prazna lista.");
					setEmptyList();
				}
				else
					{
					
					
						$.each(data, function(i,artikal){
		
						addOrder(artikal);
						AllOrdersArray.push(artikal);
		
						});
						
						doSum();

					}
			}
			  });	
		
		//F-JA DODAVANJA ARTIKLA U TABELU
		var $orders = $('#ordersTable');
		var tableItemTemplate = $('#tableItemTemplate').html();
		function addOrder(artikal)
		{
			//racunanje ukupnog 
			totalSum = totalSum + (artikal.cena* artikal.kolicina);

			
			//estetski deo
			var item = Mustache.render(tableItemTemplate, artikal);
			var $item_element = $(item);
			
			var $ItemNumber = $item_element.find("#ItemNumber");
			$ItemNumber.text(artikalCNT);
			artikalCNT++;
			
			var $orderCena = $item_element.find("#orderCena");
			$orderCena.text(Number(artikal.cena).toFixed(2))
			
			$orders.append($item_element.prop('outerHTML'));
		
			
		}
		
		//Klik na narucivanje
		$("#orderBtn").click(function () {
			
			var note = $("#noteTextField").val();
			
			note = "null";
			
			json_data = '{"artikli":' + JSON.stringify(AllOrdersArray) + ', "napomena":"' + note + '"}';
			
			console.log(json_data);
			
			//prvo sendujem koliko tokena je upisao i id korisnika
			$.ajax({
				url: "rest/orders/check/" + usedTokens,
				type: 'GET',
				success: function()
				{

				} 
			});
			
			//sendovanje svih porucenih artikala
			
			$.ajax({
				
				url: 'rest/orders/take',
				type: 'POST',
				data: json_data,
				dataType:"json",
				contentType: 'application/json',
				complete: function(data)
				{
					
					console.log(data);
					
					if(data.responseText=="")
					{
						alert("Error while taking order");
						window.location.href = "index.html";

					}
					else
					{
						alert("Za 45 minut do sat vremena stize");
						clearOrders();
						window.location.href = "index.html";
					}
					
				}
				
			});
			
		});
		
		//KLIK NA EMPTY BASKET
		$("#EmptyBasket").click(function() {
			
			clearOrders();
			location.reload();
			
		});
		

	
	
		//klik na logout
		$("#LogoutMenuItem").click(function () {
			alert("Logging out");
			$.ajax({
				type: 'GET',
				url: 'rest/users/logout',
				success: function(data)
				{
		            window.location.href = 'index.html';
				}
				  });
		});
		
		
		$("#checkPrice").click(function() {
			
			var enteredTokens = $("#tokenNumber").val();
			
			usedTokens = enteredTokens;
			
			var current_price = totalSum - (enteredTokens*0.03*totalSum);
			
			var $sumSign = $("#sumSign");
			
			$sumSign.text(Number(current_price).toFixed(2));
			
		});
	
});


//F-JA ZA PODESAVANJE IZGLEDA AKO NEMA NIJEDNOG ARTIKLA U KORPI
function setEmptyList()
{
	$("#ordersTable").hide();
	$("#totalSum").hide();
	$("#orderBtn").hide();
	$("#checkPrice").hide();
	$("#EmptyBasket").hide();
	$("#noteTextArea").hide();	
	
	$("#emptyBasketSign").show();
}


//ISPIS TOTALNE SUME
function doSum()
{
	var $sumSign = $("#sumSign");
	
	$sumSign.text(Number(totalSum).toFixed(2));
	
}

//setovanje za pojedinacnog korisnika
function setUpForAnLoggedInUser (user)
{

	var $welcomeSign = $("#welcomeSign");
	
	$welcomeSign.text(user.username + "'s profile");
	
	
	//sve dalje se odnosi na tokenizaciju
	var $tokenNumber = $("#tokenNumber");
	
	if(user.tokens<10)
		$tokenNumber.attr("max",user.tokens);
	else
		$tokenNumber.attr("max",10);

	
	var $tokensHeader = $("#tokensHeader");

	if(user.tokens==0)
	{
		$tokensHeader.text("You don't have any tokens.");
		$tokenNumber.css({"background-color": "#ff9db3"});
	}
	else
	{
		if(user.tokens<10)
			$tokensHeader.text("You have " + user.tokens  + " tokens - you can use some now. ");
		else
			$tokensHeader.text("You have 10 tokens - you can use some now. ");
	}
	
}

//FUNKCIJA ZA CISCENJE KORPE
function clearOrders()
{
	$.ajax({
		url: "rest/orders/deleteBasket",
		type: 'PUT',
		success: function()
		{

		} 
	});
}
