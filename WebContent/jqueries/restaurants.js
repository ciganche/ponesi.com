var loggedIn = false;
var loggedInDeliveryGuy = false;

$(function () {
	
	
	$.ajax({
		type: 'GET',
		url: 'rest/artikli/top10',
		success: function(data)
		{
			$("#top10").empty();
			$.each(data, function(i,artikal){

				$("#top10").append("<li>" + artikal.ime + " - " + artikal.restoran_maticni + "</li>");

				});
		}
		  });
	
	
	
	var id_presented = window.location.search.substring(4);	
	
	
	//FILTERISANJE ARTIKALA RESTORANA
	$('#doFiltering').click(function() 
			{
				
				 var ShowingArrayTemp = []; //niz restorana nakon filtera kategorije
				 var ShowingArrayTemp1 = []; //niz restorana nakon filtera kategorije i filtera imena artikla
				 var ShowingArrayFinal = []; //niz restorana nakon filtera kategorije i filtera imena i filtera imena i filtera imena restorana i maksimalne cene 
				
				//filter kategorije
				var val = $('input[name=test]:checked').val();
				if(val == "all")
				{
				 	for( i = 0 ; i < SviArtikli.length ; i++ )
					{
			    	 		ShowingArrayTemp.push(SviArtikli[i]);
					}
				}
				if(val == "meals")
				{
				 	for( i = 0 ; i < SviArtikli.length ; i++ )
					{
			    		if(SviArtikli[i].tip == "Food")
			    		{
			    			ShowingArrayTemp.push(SviArtikli[i]);
			    		}
					}
				}
				if(val == "drinks")
				{
				 	for( i = 0 ; i < SviArtikli.length ; i++ )
					{
			    		if(SviArtikli[i].tip == "Drink")
			    		{
			    	 		ShowingArrayTemp.push(SviArtikli[i]);
			    		}
					}
				}
				
				
				//Filter sadrzajnosti imena
				var uneseno_ime = $('#nameSearch').val().toLowerCase();
				if(uneseno_ime=="")
				{
			    	for( i = 0 ; i < ShowingArrayTemp.length ; i++ )
			    	{
			    		ShowingArrayTemp1.push(ShowingArrayTemp[i]);
			    	}
				}
				else
				{
			    	for( i = 0 ; i < ShowingArrayTemp.length ; i++ )
			    	{
			    		if(ShowingArrayTemp[i].ime.toLowerCase().indexOf(uneseno_ime) >= 0)
			    			ShowingArrayTemp1.push(ShowingArrayTemp[i]);
			    	}
				}
				
				
				//Filter maksimalne cene
				var uneseno_ime = $('#priceSearch').val().toLowerCase();
				if(uneseno_ime=="")
				{
			    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
			    	{
			    		ShowingArrayFinal.push(ShowingArrayTemp1[i]);
			    	}
				}
				else
				{
					
					if(!$.isNumeric(uneseno_ime)) { console.log("BAAAAM1");validiraj(); return;}
					var temp = parseInt(uneseno_ime);

			    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
			    	{
			    		if(temp >= ShowingArrayTemp1[i].cena)
				    	{
				    			ShowingArrayFinal.push(ShowingArrayTemp1[i]);
				    	}	    	
			    	}
				}
				
				
				$artikli.empty();
				//popunjavanje liste sa ShowingArrayFinal elementima
		    	for( i = 0 ; i < ShowingArrayFinal.length ; i++ )
		    	{
		    		addArticle(ShowingArrayFinal[i]);
		    	}
			 
		    	if(ShowingArrayFinal.length == 0 || SviArtikli.length == 0)
		    	{
		    		var $noElementsFound = $('#noElementsFound');
		    		$artikli.append($noElementsFound.prop('outerHTML'));
		    	    $("#noElementsFound").show();

	
		    	}
				
			});
			    
			//VALIDACIJA PRILIKOM UNOSA MAX CENE
			function validiraj ()
			{
				var $priceField = $('#priceSearch');
				$priceField.css({"background-color": "#ff9db3"});

				$priceField.attr("placeholder", "A number must be entered!");
				$('#priceSearch').val("");

			}
			$( "#priceSearch" ).focus(function(){
				
				
				$(this).css({"background-color": "white"});
				$(this).attr("placeholder", "Search by maximum price ...");
				
			});
	
	
	
	
	var $artikli = $('#artikli');
	var artikalTemplate = $('#artikalTemplate').html();
	var SviArtikli = [];

	
	//F-JA ZA DODAVANJE ARTIKLA U <UL>
	function addArticle(artikal)
	{
		
		var target_id = artikal.id;
		var item = Mustache.render(artikalTemplate, artikal);
		var $item_element = $(item);
		
		var $cenaTag =  $item_element.find("#cenaTag");		
		$cenaTag.text(Number(artikal.cena).toFixed(2));
		
		
		var $kvantitetInput =  $item_element.find("#kvantitetInput");		
		$kvantitetInput.attr("id","kvantitetInput" + artikal.id);
		
		
		var $addToOrder =  $item_element.find("#addToOrder");		
		$addToOrder.attr("id","addToOrder" + artikal.id);
		
		
		var $grami =  $item_element.find("#grami");
		if(artikal.tip == "Food")
			{
				$grami.text("Size: " + artikal.gramaza + " g");
			}
		else
			{
				$grami.text("Size: " + artikal.gramaza + " ml");
			}
		
		
		//dodavanje u <ul> 
		$artikli.append($item_element.prop('outerHTML'));
		
		//FUNKCIJA ZA DODAVANJE SELEKTOVANOG ARTIKLA U KORPU
		$("#addToOrder" + artikal.id).click(function()
		{
			
			if(loggedIn==true)
			{
			
			var id_content = $(this).attr("id");
			var id_target = id_content.substring(10);

			var id_content = $(this).attr("id");
			var id_target = id_content.substring(10);
			
			
			var kolicina = $("#kvantitetInput" + id_target).val();
			console.log(id_target + " - " + kolicina);
			
			
			//ajax zahtev smestanja u korisnicku korpu
			$.ajax({
				
				url: "rest/users/AddToBasket/" + id_target + "/" + kolicina,
				type: 'PUT',
				success: function()
				{
					alert(kolicina + " orders placed in the basket!");
				}
				
			});

			
			
				 
			}
			else //Ukoliko nije ulogovan
			{
				alert("Login is requered to place an order.");
				window.location.href = "login.html";
			}
		});

	}
	
	
	//uzimanje podataka o restoranu
	$.ajax({
		type: 'GET',
		url: 'rest/restorani/restaurants/' + id_presented,
		success: function(data)
		{
			restoran = data;
			console.log(data);
			setPage(data);
			
	    	if(data.artikli_restorana.length == 0)
	    	{
	    		var $noElementsFound = $('#noElementsFound');
	    		$artikli.append($noElementsFound.prop('outerHTML'));
	    	    $("#noElementsFound").show();	

	    	}
			
			$.each(data.artikli_restorana, function(i,artikal)
			{
				
				addArticle(artikal);
				SviArtikli.push(artikal);

			})
		}
		  });
	
	
	//gettovanje tipa korisnika: ulogovan ili ne
	$.ajax({
		type: 'GET',
		url: 'rest/users/currentUser',
		contentType: 'application/json',
		dataType:"json",
		complete: function(data)
		{
			
			var loggedUser = data.responseJSON;
			
			
			if(loggedUser != null)
			{
				loggedIn = true;
				setUpForAnLoggedInUser(loggedUser);				
			}
			else //MOZDA LI SE DOSTAVLJAC ULOGOVAO?
			{
				
				$.ajax({
					type: 'GET',
					url: 'rest/users/currentDeliveryGuy',
					contentType: 'application/json',
					dataType:"json",
					complete: function(data)
					{
						
						var loggedDeliveryGuy = data.responseJSON;
						
						
						if(loggedDeliveryGuy != null)
						{
							loggedInDeliveryGuy = true;
							setUpForAnLoggedInDeliveryGuy(loggedDeliveryGuy);				
						}
						else //MOZDA LI SE ADMIN ULOGOVAO?
						{
							
							$.ajax({
								type: 'GET',
								url: 'rest/users/currentAdmin',
								contentType: 'application/json',
								dataType:"json",
								complete: function(data)
								{
									var loggedAdmin = data.responseJSON;
									
									if(loggedAdmin!=null)
									{
										loggedAdmin = true;
										setUpALoggedAdmin(loggedAdmin);
									}
								}
							});
							
						}
					}
				
					  });
				
			}
			
		}
		  });

	
	//KLIK NA LOGOUT
	$("#LogoutMenuItem").click(function () {
		alert("Logging out");
		
		$.ajax({
			type: 'GET',
			url: 'rest/users/logout',
			success: function(data)
			{
			    location.reload();
			}
			  });
	});
	
	

	
});


//NAKON LOGOVANJA KORISNIKA
function setUpForAnLoggedInUser (user)
{
	var $MyProfileMenuItem = $("#MyProfileMenuItem");
	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $HistoryMenuItem = $("#HistoryMenuItem");
	var $MyProfileMenuItem_aTag = $MyProfileMenuItem.find("a");
	var $welcomeSign = $("#welcomeSign");
	
	$LoginMenuItem.hide();
	$MyProfileMenuItem.show();
	$LogoutMenuItem.show();
	$HistoryMenuItem.show();
	$welcomeSign.text(user.username + "'s profile");
	
	$MyProfileMenuItem_aTag.attr("href","MyOrders.html");	
}


//NAKON UCITAVANJA STRANICE
function setPage(restoran)
{
	//getting 
	var $titlesign = $('#titlesign');
	var $descsign = $('#descsign');

	
	//setting 
	$titlesign.text(restoran.ime);
	$descsign.text(restoran.tip_str);
}


function setUpForAnLoggedInDeliveryGuy (deliveryGuy)
{

	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $mealsDrinks = $("#mealsDrinks");
	var $deliveriesMenuItem = $("#deliveriesMenuItem");
	
	
	$deliveriesMenuItem.show();
	$mealsDrinks.hide();
	$LoginMenuItem.hide();
	$LogoutMenuItem.show();

	$(".narucuj").hide();
}

function setUpALoggedAdmin(admin)
{
	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $welcomeSign = $("#welcomeSign");
	var $mealsDrinks = $("#mealsDrinks");
	var $deliveriesMenuItem = $("#deliveriesMenuItem");
	var $manageMenuItem = $("#manageMenuItem");
	
	$LogoutMenuItem.show();
	$manageMenuItem.show();
	$mealsDrinks.hide();
	$LoginMenuItem.hide();
	
	$(".narucuj").hide();
}



