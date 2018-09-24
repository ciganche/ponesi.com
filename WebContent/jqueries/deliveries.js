var loggedInDeliveryGuy = false;

var unaccepted_orders = [];
var accepted_orders = [];

//VOZILO
var vehicle_selected = false;
var vehicle_selected_id;
$(function () {
	
	
	function setVehicleChoiceIrrelevant()
	{
		var $veh = $("#vehicleSelection");
		$veh.hide();
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
	
	
	//PUNJENJE COMBOBOXA SA SLOBODNIM VOZILIMA
	var $vehicleSelectionCombobox = $("#vehicleSelectionCombobox");
	$.ajax({
		type: 'GET',
		url: 'rest/vozila/',
		contentType: 'application/json',
		dataType:"json",
		success: function(data)
		{
			$.each(data, function(i,vozilo){
				console.log(vozilo);
				if(vozilo.zauzeto == 0)
					$vehicleSelectionCombobox.append('<option value="' + vozilo.id + '">' + vozilo.marka + " " + vozilo.model + "</option>");
			});
		}	
	});
	
	
	
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
				
				$.ajax({
					type: 'GET',
					url: 'rest/orders/',
					success: function(data)
					{						
						$.each(data, function(i,order) {
							
							
							//AKO NIJE NIKOME DODELJENO IDE NA CENTRALNI DEO EKRANA
							if(order.stanje == 0) 
							{
								unaccepted_orders.push(order);
								addOrder(order);
							}
							
							//AKO JE DODELJENO OVOM DOSTAVLJACU IDE NA DESNU STRANU
							if(order.dostavljac_id == loggedDeliveryGuy.id)
							{
								//UBACI U LISTU DESNE STRANE
								
								//NACI KORISNIKA KOJI JE PORUCIO PRVO
								$.ajax({
									type: 'GET',
									url: 'rest/users/getUserById/' + order.user,
									contentType: 'application/json',
									dataType:"json",
									complete: function(data)
									{
										var user = data.responseJSON;
										
										
										//AJAX ZA GETTOVANJE VEOZILA PO ID-U loggedDeliveryGuy.voziloId
										console.log("Init - ubacivanje ordera dodeljenih dostavljacu - voziloId " + loggedDeliveryGuy.voziloId);
										$.ajax({
											
											type: 'GET',
											url: 'rest/vozila/getVehicle/' + loggedDeliveryGuy.voziloId,
											contentType: 'application/json',
											dataType:"json",
											complete: function(data)
											{
												
												var vehicle = data.responseJSON;
												console.log("Init - ubacivanje ordera dodeljenih dostavljacu - vozilo: " + vehicle.marka + " - " + vehicle.model )
												InsertAcceptedOrder(order,user, vehicle.marka + " - " + vehicle.model);
											}
											
										});
										
										
									}
								});
								
							}
							
							});
						
						
						
						if(unaccepted_orders.length == 0)
						{
							setEmptyList();
						}
						
					} //success
					  });
				
			}
			else
			{
				window.location.href = "index.html";
			}

		}
	
		  });
	

	
	function addOrder(order)
	{
		
		var $ordersToAccept = $("#ordersToAccept");
		var orderToAcceptTemplate = $('#orderToAcceptTemplate').html();
		var $item_element = $(orderToAcceptTemplate);



		//GETTOVANJE KORISNIKA KOJI JU JE NARUCIO
		$.ajax({
			type: 'GET',
			url: 'rest/users/getUserById/' + order.user,
			contentType: 'application/json',
			dataType:"json",
			complete: function(data)
			{
				var user = data.responseJSON;
				
				
				var $StreetDateSign = $item_element.find("#StreetDateSign");
				$StreetDateSign.text(user.name + " " + user.lastname + " - " + order.dateTime_order);
				
				var $ordersInfo = $item_element.find("#ordersInfo");			
				$.each(order.artikli, function(i,artikal){
					$ordersInfo.append('<li>' + artikal + '</li>');
				});
				
				var $orderNote = $item_element.find("#orderNote");
				if(order.napomena != "null")
				{
					$orderNote.text("Special request: " + order.napomena);
				}
				else
				{
					$orderNote.text("Special request: none");
				}
				var $userContact = $item_element.find("#userContact");
				$userContact.text("Contact: " + user.tel);
				
				
				var $totalCost = $item_element.find("#totalCost");
				$totalCost.text("Total cost: " +  Number(order.total_cena).toFixed(2));
				
				
				/*
				 * DODATI ZA NAPOMENU JEDAN <span>
				 * 
				 * 
				 * *********************************
				 * 
				 * 
				 * 
				 */
				
				var $acceptOrderBtn = $item_element.find('#acceptOrderBtn');
				$acceptOrderBtn.attr("id","acceptOrderBtn" + order.id);
				
				var $lii = $item_element.find('#orderToAcceptLi');
				$lii.attr("id","orderToAcceptLi" + order.id);

				$ordersToAccept.append($item_element.prop('outerHTML'));
				
				
				
				$("#acceptOrderBtn" + order.id).click(function () {
					
					
					if(vehicle_selected == false)
					{
						alert("You must select an available vehicle before accepting orders.");
					}
					else
					{	
			
						//
						if(accepted_orders.length == 0)
						{
							//ajax zahtev za uzimanje slobodnog vozila
							
							$.ajax({
								type: 'PUT',
								url: 'rest/vozila/takeVehicle/' + vehicle_selected_id,
								success: function()
								{
									console.log("poslao za vozilo: " + vehicle_selected_id);
								}
								  });		
						}
						
						setVehicleChoiceIrrelevant();

						
						$("#orderToAcceptLi" + order.id).hide();
						
												
						console.log(vehicle_selected_id);
						
						//GETTOVANJE AJAXOM NAZIVA VOZILA PREKO ID-A
						$.ajax({
							
							type: 'GET',
							url: 'rest/vozila/getVehicle/' + vehicle_selected_id,
							contentType: 'application/json',
							dataType:"json",
							complete: function(data)
							{
								
								var vehicle = data.responseJSON;
								console.log("Init - ubacivanje ordera dodeljenih dostavljacu - vozilo: " + vehicle.marka + " - " + vehicle.model );
								InsertAcceptedOrder(order,user, vehicle.marka + " - " + vehicle.model);
							}
							
						});
						
						
						
						
						//IZBACIVANJE IZ REDA
						unaccepted_orders.splice( $.inArray(order, unaccepted_orders), 1 );						
						if(unaccepted_orders.length == 0)
						{
							setEmptyList();
						}
						
						
						
						//SLANJE ZAHTEVA ZA DODAVANJE ID-A OVE NARUDZBINE U LISTU ID NARUDZBINA OVOG DELIVERY GUYJA
						$.ajax({
							type: 'PUT',
							url: 'rest/orders/InsertToDeliveryGuysOrders/'+order.id,
							success: function()
							{
								console.log("poslao zahtev.");
							}
							  });
				
					}
				});
				
			}
			  });
		


		
	}
	
	$("#vehicleSelectionCombobox").change(function () {
		if($("#vehicleSelectionCombobox option:selected").val() != "no")
		{
			vehicle_selected = true;
			vehicle_selected_id = $("#vehicleSelectionCombobox option:selected").val();
			
			console.log(vehicle_selected_id);
		}
	});
	
	
	function setEmptyList()
	{
		var $noOrdersToAccept = $("#noOrdersToAccept");
		$noOrdersToAccept.show();
	}
	

	
	function setUpForAnLoggedInDeliveryGuy(loggedDeliveryGuy)
	{
		var $welcomeSign = $("#welcomeSign");
		$welcomeSign.text(loggedDeliveryGuy.username + "'s buisness profile");
		
		
		if(loggedDeliveryGuy.order_id.length != 0)
		{

			setVehicleChoiceIrrelevant();
			vehicle_selected_id = loggedDeliveryGuy.voziloId;
			vehicle_selected = true;
			
			console.log("Prilikom setUpForAnLoggedInDeliveryGuy user.voziloId: " + loggedDeliveryGuy.voziloId);
			
		}
		else
		{
			vehicle_selected = false;
		}

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	function InsertAcceptedOrder(order,user, vehicleSelectedName)
	{
		
		accepted_orders.push(order);
		
		
		var acceptedOrderTemplate = $("#acceptedOrderTemplate").html();
		var $acceptedOrders = $("#acceptedOrders");
		
			
		var $item = $(acceptedOrderTemplate);
		
		
		//elementi - gettovanje
		var $item_li = $item.find("#acceptedOrderLi");
		$item_li.attr("id","acceptedOrderLi" + order.id);
		
		var $acceptedOrderTitle = $item.find("#acceptedOrderTitle");
		var $acceptedOrderTo = $item.find("#acceptedOrderTo");
		var $acceptedOrderContact = $item.find("#acce-AptedOrderContact");
		var $acceptedOrderCost = $item.find("#acceptedOrderCost");
		var $acceptedOrdersInfo = $item.find("#acceptedOrdersInfo");
		var $vehicleTaken = $item.find("#vehicleTaken");
		var $markAsDelivered =  $item.find("#markAsDelivered");
		var $markAsCancled =  $item.find("#markAsCancled");
		
		
		//elementi - settovanje
		$acceptedOrderTitle.text("Order to deliver: ");
		$acceptedOrderTo.text("To: " + user.name + " " + user.lastname);
		$acceptedOrderContact.text("Contact: " + user.tel);
		$acceptedOrderCost.text("Total: " + Number(order.total_cena).toFixed(2));
		 
		
		$.each(order.artikli, function(i,artikal){
			$acceptedOrdersInfo.append('<li> <a class="selectCategorie" >' + artikal + '</a></li>');
		});
		$vehicleTaken.text('Vehicle taken: ' + vehicleSelectedName);
		
		$markAsDelivered.attr("id","markAsDelivered" + order.id);
		$markAsCancled.attr("id","markAsCancled" + order.id);
		
		$acceptedOrders.append($item.prop('outerHTML'));
		
		
		$("#markAsCancled" + order.id).click(function() {
			
			//AJAX ZAHTEV ZA PROMENU STATUS U OTKAZANO I BRISANJE ORDERA IZ DOSTAVLJACA
			$.ajax({
				type: 'PUT',
				url: 'rest/orders/MarkAsCancled/'+order.id,
				success: function()
				{
					console.log("poslao zahtev obelezavanje otkazanog.");
				}
				  });
			
			
			
			
			$("#acceptedOrderLi" + order.id).hide();
			
			accepted_orders.splice( $.inArray(order, accepted_orders), 1 );						
			
			if(accepted_orders.length == 0)
			{
				//ajax vrati vozilo
				//UZIMAM ULOGOVANOG DELIVERY GUYA DA BIH IZVUKAO ID VOZILA KOJ JE UZEO
				$.ajax({
					type: 'GET',
					url: 'rest/users/currentDeliveryGuy',
					contentType: 'application/json',
					dataType:"json",
					complete: function(data)
					{
					
						var loggedDeliveryGuy = data.responseJSON;
						
						console.log(loggedDeliveryGuy);
						
						//VRACAM VOZILO
						$.ajax({
							type: 'PUT',
							url: 'rest/vozila/returnVehicle/' + loggedDeliveryGuy.voziloId,
							success: function()
							{
								console.log("vraca vozilo: " + loggedDeliveryGuy.voziloId);
								window.location.reload();
							}
							  });		
					}
				});

				
				vehicle_selected = false;
				$("#vehicleSelection").show();
			}
		});
		
		$("#markAsDelivered" + order.id).click(function() {
			
			//AJAX ZAHTEV ZA PROMENU STATUS U OTKAZANO I BRISANJE ORDERA IZ DOSTAVLJACA
			$.ajax( {
				type: 'PUT',
				url: 'rest/orders/MarkAsDelivered/'+order.id,
				success: function()
				{
					console.log("poslao zahtev obelezavanje dostavljenog.");
				}
				  });
			
			
			$("#acceptedOrderLi" + order.id).hide();
			
			
			accepted_orders.splice( $.inArray(order, accepted_orders), 1 );						
			
			if(accepted_orders.length == 0)
			{
				//UZIMAM ULOGOVANOG DELIVERY GUYA DA BIH IZVUKAO ID VOZILA KOJ JE UZEO
				$.ajax({
					type: 'GET',
					url: 'rest/users/currentDeliveryGuy',
					contentType: 'application/json',
					dataType:"json",
					complete: function(data)
					{
					
						var loggedDeliveryGuy = data.responseJSON;
						
						console.log(loggedDeliveryGuy);

						//VRACAM VOZILO
						$.ajax({
							type: 'PUT',
							url: 'rest/vozila/returnVehicle/' + loggedDeliveryGuy.voziloId,
							success: function()
							{
								console.log("vraca vozilo: " + loggedDeliveryGuy.voziloId);
								window.location.reload();

							}
							  });		
					}
				});
				
				vehicle_selected = false;
				$("#vehicleSelection").show();
			
			}
			
		});
	}

	
	
	
	
	
	
	
	
});





