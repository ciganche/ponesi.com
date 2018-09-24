var loggedIn = false;

$(function ()  {
	
	
	//F-JA ZA DODAVANJE HISTORY ITEMA
	var $historyItems = $('#historyItems');
	var historyItemTemplate = $('#historyItemTemplate').html();
	function addHistoryItem(historyItem)
	{
		var item = Mustache.render(historyItemTemplate, historyItem);
		var $item_element = $(item);
		
		//prikaz cene
		var $totalCost = $item_element.find("#totalCost");
		$totalCost.text("Total cost: " + Number(historyItem.cost).toFixed(2));
		
		//prikaz svakog itema iz te porudzbine
		var $ordersInfo = $item_element.find("#ordersInfo");
		
		$.each(historyItem.orderInfos, function(i,orderInfo)
				{
					$ordersInfo.append('<li> <p style="margin-bottom: 1%;">' + orderInfo +'</p> </li>');
				});
		
		$historyItems.append($item_element.prop('outerHTML'));
	}
	
	//KLIK NA LOGOUT
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
			else
			{
				window.location.href = "index.html";
			}
		}
		  });
	
	

	//GETTOVATI ISTORIJU
	
	//GETTOVATI TAGOVANE RESTORANE IZ SESIJE
	
	
	
	function setUpForAnLoggedInUser (user)
	{

		var $MyProfileMenuItem = $("#MyProfileMenuItem");
		var $LogoutMenuItem = $("#LogoutMenuItem");
		var $LoginMenuItem = $("#LoginMenuItem");
		var $welcomeSign = $("#welcomeSign");
		var $noHistoryFound = $("#noHistoryFound");
		
		$LogoutMenuItem.show();
		$LoginMenuItem.hide();
		$MyProfileMenuItem.show();
		$welcomeSign.text(user.username + "'s profile");
		
		
		
		//UBACIVANJE HISTORY ITEM-A
		
		if(user.history.length == 0)
		{
			$noHistoryFound.show();
		}
		$.each(user.history, function(i,historyItem){
			
			addHistoryItem(historyItem);
			
			});		
		
		//UBACIVANJE PINOVANIH RESTORANA
		var $pinnedRestaurants = $("#pinnedRestaurants");
		$.each(user.saved, function(i,pinnedRestaurant_id){
			
			$.ajax({
				type: 'GET',
				url: 'rest/restorani/restaurants/' + pinnedRestaurant_id,
				success: function(data)
				{
					restoran = data;
					
					$pinnedRestaurants.append('<li><a href="restaurants.html?id=' + pinnedRestaurant_id + '">' + data.ime + "</a></li>")


				}
				  });
			
			
			
			
			
			});	
		
	}
	
	
});





