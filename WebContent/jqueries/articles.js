var loggedIn = false;

$ (function () {
		
	
	var SviArtikliArray = [];
	var $artikli = $("#artikli");
	var artikalTemplate = $("#artikalTemplate").html();
	
	
	//F-JA ZA DODAVANJE ARTIKLA
	function addArtikal(artikal)
	{
		var item = Mustache.render(artikalTemplate, artikal);
		
		var $item_element = $(item);
		var $cenaTag =  $item_element.find("#cenaTag");
		var $restaurantTag =  $item_element.find("#restaurantTag");
		
		$restaurantTag.text("From: " + artikal.restoran_maticni);
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
	
	
	//gettovanje artikala
	$.ajax({
		type: 'GET',
		url: 'rest/artikli/',
		success: function(data)
		{
			$.each(data, function(i,artikal){

				addArtikal(artikal);
				SviArtikliArray.push(artikal);
				})
				
				
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
			
			console.log(data.responseJSON);
			
			if(loggedUser != null)
			{
				loggedIn = true;
				setUpForAnLoggedInUser(loggedUser);
			}
		}
		  });
	
	
	
	
	//FILTERISANJE ARTIKLA
	$('#doFiltering').click(function() 
	{
		
		 var ShowingArrayTemp = []; //niz restorana nakon filtera kategorije
		 var ShowingArrayTemp1 = []; //niz restorana nakon filtera kategorije i filtera imena artikla
		 var ShowingArrayTemp2 = []; //niz restorana nakon filtera kategorije i filtera imena artikla i imena restorana
		 var ShowingArrayFinal = []; //niz restorana nakon filtera kategorije i filtera imena i filtera imena i filtera imena restorana i maksimalne cene 
		
		//filter kategorije
		var val = $('input[name=test]:checked').val();
		if(val == "all")
		{
		 	for( i = 0 ; i < SviArtikliArray.length ; i++ )
			{
	    	 		ShowingArrayTemp.push(SviArtikliArray[i]);
			}
		}
		if(val == "meals")
		{
		 	for( i = 0 ; i < SviArtikliArray.length ; i++ )
			{
	    		if(SviArtikliArray[i].tip == "Food")
	    		{
	    	 		ShowingArrayTemp.push(SviArtikliArray[i]);
	    		}
			}
		}
		if(val == "drinks")
		{
		 	for( i = 0 ; i < SviArtikliArray.length ; i++ )
			{
	    		if(SviArtikliArray[i].tip == "Drink")
	    		{
	    	 		ShowingArrayTemp.push(SviArtikliArray[i]);
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
		
		
		
		//Filter sadrzajnosti imena restorana
		var uneseno_ime = $('#restaurantSearch').val().toLowerCase();
		if(uneseno_ime=="")
		{
	    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
	    	{
	    		ShowingArrayTemp2.push(ShowingArrayTemp1[i]);
	    	}
		}
		else
		{
	    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
	    	{
	    		if(ShowingArrayTemp1[i].restoran_maticni.toLowerCase().indexOf(uneseno_ime) >= 0)
	    			ShowingArrayTemp2.push(ShowingArrayTemp1[i]);
	    	}
		}
		
		
		//Filter maksimalne cene
		var uneseno_ime = $('#priceSearch').val().toLowerCase();
		if(uneseno_ime=="")
		{
	    	for( i = 0 ; i < ShowingArrayTemp2.length ; i++ )
	    	{
	    		ShowingArrayFinal.push(ShowingArrayTemp2[i]);
	    	}
		}
		else
		{
			
			if(!$.isNumeric(uneseno_ime)) { console.log("BAAAAM1");validiraj(); return;}
			var temp = parseInt(uneseno_ime);

	    	for( i = 0 ; i < ShowingArrayTemp2.length ; i++ )
	    	{
	    		if(temp >= ShowingArrayTemp2[i].cena)
		    	{
		    			ShowingArrayFinal.push(ShowingArrayTemp2[i]);
		    	}	    	
	    	}
		}
		
		
		$artikli.empty();
		//popunjavanje liste sa ShowingArrayFinal elementima
    	for( i = 0 ; i < ShowingArrayFinal.length ; i++ )
    	{
    		addArtikal(ShowingArrayFinal[i]);
    	}
	 
    	if(ShowingArrayFinal.length == 0)
    	{
    		var $noElementsFound = $('#noElementsFound');
    		$restorani.append($noElementsFound.prop('outerHTML'));
    	    $("#noElementsFound").show();

    		
    	}
		
	});
	    
	//FRONTEND VALIDACIJA MAX PRICE POLJA
	function validiraj ()
	{
		var $priceField = $('#priceSearch');
		$priceField.css({"background-color": "#ff9db3"});

		$priceField.attr("placeholder", "A number must be entered!");
		$('#priceSearch').val("");

	}
	$( "#priceSearch" ).focus(function() {
		
		$(this).css({"background-color": "white"});
		$(this).attr("placeholder", "Search by maximum price ...");
		
	});
	
	
	
	//F-JA ZA LOGOUT
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
	
	

	
		
} );

//NAKON LOGOVANJA KORISNIKA
function setUpForAnLoggedInUser (user)
{
	var $MyProfileMenuItem = $("#MyProfileMenuItem");
	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $MyProfileMenuItem_aTag = $MyProfileMenuItem.find("a");
	var $welcomeSign = $("#welcomeSign");
	var $HistoryMenuItem = $("#HistoryMenuItem");
	
	$LoginMenuItem.hide();
	$MyProfileMenuItem.show();
	$LogoutMenuItem.show();
	$HistoryMenuItem.show();
	$welcomeSign.text(user.username + "'s profile");
	
	$MyProfileMenuItem_aTag.attr("href","MyOrders.html");	
}