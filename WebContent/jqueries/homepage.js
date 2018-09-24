
var loggedIn = false;
var loggedInDeliveryGuy = false;
var loggedInAdmin = false;

var users_pinned_resautrant = [];

$ (function () {
	
	
	$.ajax({
		type: 'GET',
		url: 'rest/artikli/',
		complete: function(asdf)
		{
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
	
	
	
	
	

	
	
	$('#doFiltering').click(function() 
	{
		
	     $restorani.empty(); // praznjenje iskesirane liste restorana
		 var ShowingArrayTemp = []; //niz restorana nakon filtera kategorije
		 var ShowingArrayTemp1 = []; //niz restorana nakon filtera kategorije i filtera imena
		 var ShowingArrayFinal = []; //niz restorana nakon filtera kategorije i filtera imena i filtera ulice
		 
		 
	 	
	 	
	 	//Filter kategorije
		var val = $('input[name=test]:checked').val();
		
		if(val == "all")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
			}
		}
		
		if(val == "serbian")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "Serbian")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
	    		}
			}
		}
		if(val == "bbq")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "BBQ")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
	    		}
			}
		}	
		if(val == "chinese")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "Chinese")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
	    		}
			}
		}
		if(val == "indian")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "Indian")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
	    		}
			}
		}
		if(val == "pizza")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "Pizza")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
	    		}
			}
		}
		if(val == "pastaria")
		{
		 	for( i = 0 ; i < SviRestoraniArray.length ; i++ )
			{
	    		if(SviRestoraniArray[i].tip_str == "Pastaria")
	    		{
	    	 		ShowingArrayTemp.push(SviRestoraniArray[i]);
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
		
		
		//Filter sadrzajnosti imena ulice
		var uneseno_ime = $('#streetSearch').val().toLowerCase();
		if(uneseno_ime=="")
		{
	    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
	    	{
	    		ShowingArrayFinal.push(ShowingArrayTemp1[i]);
	    	}
		}
		else
		{
			
	    	for( i = 0 ; i < ShowingArrayTemp1.length ; i++ )
	    	{
	    		if(ShowingArrayTemp1[i].ulica.toLowerCase().indexOf(uneseno_ime) >= 0)
		    	{
		    			ShowingArrayFinal.push(ShowingArrayTemp1[i]);
		    	}	    	
	    	}
		}
		
		
		//popunjavanje liste sa ShowingArrayFinal elementima
    	for( i = 0 ; i < ShowingArrayFinal.length ; i++ )
    	{
    		addRestoran(ShowingArrayFinal[i]);
    	}
	 
    	if(ShowingArrayFinal.length == 0)
    	{
    		var $noElementsFound = $('#noElementsFound');
    		$restorani.append($noElementsFound.prop('outerHTML'));
    	    $("#noElementsFound").show();

    		
    	}
		
	});
	
	
	//GETTOVANJE TOP 10 ARTIKALA
 
	
	
	
	
	var $pageSign = $('#titlesign');
	var $restorani = $('#restorani');
	var restoranTemplate = $('#restoranTemplate').html();
	var SviRestoraniArray = [];


	function addRestoran(restoran)
	{
		
		var target_id = restoran.id;
		
		var item = Mustache.render(restoranTemplate, restoran);
		
		var $item_element = $(item);
		
		var $a_tag = $item_element.find('#MenuViewingLink');
		
		$a_tag.attr("href", "restaurants.html?id=" + restoran.id);
		
		
		//PIN BUTTON: OFF I ON FUNKCIONALNOSTI, SAMO AKO JE KORISNIK ULOGOVAN
		if(loggedIn == true)
		{
			var $pinToggle = $item_element.find('#pinToggle');
			$pinToggle.attr("id","pinToggle" + restoran.id);

			if(jQuery.inArray(restoran.id, users_pinned_resautrant ) >= 0) //ako je pinnovan
			{
				$pinToggle.text("UNPIN");
			}
			else //ako nije pinnovan
			{
				$pinToggle.text("PIN");
			}
		}
		else
		{
			var $pinToggle = $item_element.find('#pinToggle');

			$pinToggle.hide();
		}
		
		//F-JA ZA KLIK NA PINOVANJE 	
		$restorani.append($item_element.prop('outerHTML'));

		
		$("#pinToggle" + restoran.id).click(function () {
			
			var odluka = $(this).text();
			
			$pinToggle = $(this);
			
			if(odluka == "PIN")
			{
				$.ajax({
					type: 'PUT',
					url: 'rest/users/insertToPinned/'+restoran.id,
					success: function()
					{
						$pinToggle.text("UNPIN");
					}
					  });
			}
			
			if(odluka == "UNPIN")
			{
				$.ajax({
					type: 'PUT',
					url: 'rest/users/deleteFromPinned/'+restoran.id,
					success: function()
					{
						$pinToggle.text("PIN");
					}
					  });
			}
			
			
			
			/*
			//AKO JE PINOVAN
			if(jQuery.inArray(restoran.id, users_pinned_resautrant ) >= 0) //ako je pinnovan
			{
				$.ajax({
					type: 'PUT',
					url: 'rest/users/deleteFromPinned/'+restoran.id,
					success: function()
					{
						location.reload();
					}
					  });
			}	
			else //AKO JE NIJE PINOVAN
			{	
				$.ajax({
					type: 'PUT',
					url: 'rest/users/insertToPinned/'+restoran.id,
					success: function()
					{
						location.reload();
					}
					  });
			}
			*/
		});
		
	
		
		
		
		

		

		
	}
	
	
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
	
	//gettovanje tipa korisnika: ulogovan ili ne\
	
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
										loggedInAdmin = true;
										setUpALoggedAdmin(loggedAdmin);
									}
								}
							});
							
						}
					}
				
					  });
				
			}
			
			getAllRestaurants();
		}
		  });
	
	
	
	//gettovanje restorana

function getAllRestaurants()
	{
		$.ajax({
			type: 'GET',
			url: 'rest/restorani/',
			success: function(data)
			{
				$.each(data, function(i,restoran){
	
					addRestoran(restoran);
					SviRestoraniArray.push(restoran);
	
					})
			}
			  });
	}


} ); 


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
	
	$.each(user.saved, function(i,savedRestaurantId){
		
		users_pinned_resautrant.push(savedRestaurantId);
		
		});		
	
}



function setUpForAnLoggedInDeliveryGuy (deliveryGuy)
{

	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $welcomeSign = $("#welcomeSign");
	var $mealsDrinks = $("#mealsDrinks");
	var $deliveriesMenuItem = $("#deliveriesMenuItem");
	
	
	$deliveriesMenuItem.show();
	$mealsDrinks.hide();
	$LoginMenuItem.hide();
	$LogoutMenuItem.show();
	$welcomeSign.text(deliveryGuy.username + "'s buisness profile");	
}

function setUpALoggedAdmin(admin)
{
	
	console.log(admin);
	
	var $LogoutMenuItem = $("#LogoutMenuItem");
	var $LoginMenuItem = $("#LoginMenuItem");
	var $welcomeSign = $("#welcomeSign");
	var $mealsDrinks = $("#mealsDrinks");
	var $deliveriesMenuItem = $("#deliveriesMenuItem");
	var $manageMenuItem = $("#manageMenuItem");
	
	var $LogoutMenuItem = $("#LogoutMenuItem");
	
	$LogoutMenuItem.show();
	$manageMenuItem.show();
	$LoginMenuItem.hide();
	$welcomeSign.text(admin.username + "'s administration profile");	
	$mealsDrinks.hide();
}




