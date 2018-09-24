var restoran_cnt = 1;
var artikal_cnt = 1;
var vozilo_cnt = 1;
var user_cnt = 1;
var order_cnt = 1;

var sakupljeni_string = "";

$(function () {
	
	clearEditSelection()
	$("input").focus(function(){
		$(this).css("background-color","white");
		$(this).attr("placeholder","");
	});
	$("select").focus(function(){
		$(this).css("background-color","white");
	});
	
	
	//GETTOVANJE ADMINA
	$.ajax({
		
		type: 'GET',
		url: 'rest/users/currentAdmin',
		contentType: 'application/json',
		dataType: "json",
		complete: function (data)
		{
			var loggedAdmin = data.responseJSON;
			if(loggedAdmin == null)
			{
				window.location.href = "index.html";
			}
			console.log("idemoo");
			var $welcomeSign = $("#welcomeSign");
			$welcomeSign.text(loggedAdmin.username + "'s administration profile");
		}
		
	});
	
	
	//GETTOVANJE RESTORANA
	$.ajax({
		type: 'GET',
		url: 'rest/restorani/',
		success: function(data)
		{
			$.each(data, function(i,restoran){

				
				addRestaurant(restoran);
				});
			
		}
		  });
	
	//GETTOVANJE ARTIKALA
	$.ajax({
		type: 'GET',
		url: 'rest/artikli/',
		success: function(data)
		{
			$.each(data, function(i,artikal){

				addArtikal(artikal);
				
			
			});
		}
		});
	
	//GETTOVANJE VOZILA
	$.ajax({
		type: 'GET',
		url: 'rest/vozila/',
		success: function(data)
		{
			$.each(data, function(i,vozilo){

				addVozilo(vozilo);
				addVoziloToList(vozilo);
			
			});
		}
		});
	
	//GETTOVANJE SVIH TIPOVA KORISNIKA
	$.ajax({
		type: 'GET',
		url: 'rest/users/getAllUsers',
		success: function(data)
		{
			$.each(data, function(i,user){

				addUserToList(user);
				addUser(user);
				
			
			});
		}
		});
	$.ajax({
		type: 'GET',
		url: 'rest/users/getAllDeliveryGuys',
		success: function(data)
		{
			$.each(data, function(i,user){

				addUser(user);
				addDeliveryGuyToList(user);
			
			});
		}
		});
	$.ajax({
		type: 'GET',
		url: 'rest/users/getAllAdmins',
		success: function(data)
		{
			$.each(data, function(i,user){

				addUser(user);
				
			
			});
		}
		});
	
	//GETTOVANJE SVIH ORDERA
	$.ajax({
		type: 'GET',
		url: 'rest/orders/',
		success: function(data)
		{
			$.each(data, function(i,order){

				addOrder(order);
				
			
			});
		}
		});
	
	
	
	//PRIKAZIVANJE DOSTAVLJACA I VOZILA AKO SE MENJA U DELIVERING TYPE
	$("#orderStatusSelect").change(function (){
		
		
		
		if($("#orderStatusSelect option:selected").val() == 1)
		{
			console.log("ideeeeeee?");
			$("#samoZaDelivering").show();
		}
		else
		{
			$("#samoZaDelivering").hide();
		}
	});
	
	
	//PROMENA PRIKAZA SADRZAJA COMBOBOX
	$("#contentSelection").change(function ()
	{	 
		//KESHIRANI DIV-OVI
		var $restaurantContent = $("#restaurantContent");
		var $artikalContent = $("#artikalContent");
		var $vozilaContent = $("#vozilaContent");
		var $userContent = $("#userContent");
		var $ordersContent = $("#ordersContent");
		
		clearEditSelection();
		
		if($("#contentSelection option:selected").val() == "restaurants")
		{
			$restaurantContent.show();
			
			//SAKRIVANJE
			$artikalContent.hide();
			$vozilaContent.hide();
			$userContent.hide();
			$ordersContent.hide();
		}
		if($("#contentSelection option:selected").val() == "artikli")
		{
			
			$artikalContent.show();
			
			//SAKRIVANJE
			$restaurantContent.hide();
			$vozilaContent.hide();
			$userContent.hide();
			$ordersContent.hide();
		}
		if($("#contentSelection option:selected").val() == "vozila")
		{
			$vozilaContent.show();
			
			//SAKRIVANJE
			$restaurantContent.hide();
			$artikalContent.hide();
			$userContent.hide();
			$ordersContent.hide();
		}
		if($("#contentSelection option:selected").val() == "users")
		{
			$userContent.show();
			
			//SAKRIVANJE
			$restaurantContent.hide();
			$artikalContent.hide();
			$vozilaContent.hide();
			$ordersContent.hide();
		}
		if($("#contentSelection option:selected").val() == "orders")
		{
			$ordersContent.show();
			
			//SAKRIVANJE
			$restaurantContent.hide();
			$artikalContent.hide();
			$vozilaContent.hide();
			$userContent.hide();
		}
	}
	);
	
	
	//NOVI RESTORAN - PRIKAZ DIALOGA; SAKRIVANJE ZA IZMENU
	$("#addRestaurant").click(function (){
		
		$("tr").css("background-color","white");
		var $addRestaurantDiv = $("#addRestaurantDiv");
		var $editRestaurantDiv = $("#editRestaurantDiv");
		
		$addRestaurantDiv.show();
		
		$editRestaurantDiv.hide();
		$("#addRestaurant").hide();	
	});
	//IZMENA RESTORANA - PRIKAZ DIALOGA; SAKRIVANJE ZA NOVI
	function restoranEditShow()
	{
		$("input").css("background-color","white");
		$("input").attr("placeholder","");
		$("select").css("background-color","white");
		
		
		var $addRestaurantDiv = $("#addRestaurantDiv");
		var $editRestaurantDiv = $("#editRestaurantDiv");
		
		$editRestaurantDiv.show();
		$("#addRestaurant").show();
		
		$addRestaurantDiv.hide();
	}
	
	
	
	
	//NOVI ARTIKAL - PRIKAZ DIALOGA, SAKRIVANJE ZA IZMENU
	$("#addArtikal").click(function () {
		
		$("tr").css("background-color","white");
		var $editArtikalDiv = $("#editArtikalDiv");
		var $addArtikalDiv = $("#addArtikalDiv");
		
		$addArtikalDiv.show();
		$editArtikalDiv.hide();
		$("#addArtikal").hide();
	});
	//IZMENA ARTIKLA - PRIKAZ DIALOGA; SAKRIVANJE NOVOG
	function artikalEditShow()
	{
		var $editArtikalDiv = $("#editArtikalDiv");
		var $addArtikalDiv = $("#addArtikalDiv");
		
		$editArtikalDiv.show();
		$("#addArtikal").show();
		
		$addArtikalDiv.hide();
	}
	
	
	//NOVO VOZILO - PRIKAZ DIALOGA
	$("#addVozilo").click(function () {
		
		$("tr").css("background-color","white");
		var $addVoziloDiv = $("#addVoziloDiv");
		var $editVoziloDiv = $("#editVoziloDiv");
		
		$addVoziloDiv.show();
		$editVoziloDiv.hide();
		$("#addVozilo").hide();
	});
	//IZMENA VOZILA - PRIKAZ DIALOGA
	function voziloEditShow()
	{
		var $addVoziloDiv = $("#addVoziloDiv");
		var $editVoziloDiv = $("#editVoziloDiv");
		
		$editVoziloDiv.show();
		$("#addVozilo").show();
		
		$addVoziloDiv.hide();
	}
	
	//NOVI ORDER - PRIKAZ DIALOGA
	$("#addOrder").click(function () 
	{
		
		$("tr").css("background-color","white");
		var $addOrderDiv = $("#addOrderDiv");
		var $editOrderDiv = $("#editOrderDiv");
		
		$addOrderDiv.show();
		$editOrderDiv.hide();
		$("#addOrder").hide();
		
	});
	function orderEditShow()
	{
		var $addOrderDiv = $("#addOrderDiv");
		var $editOrderDiv = $("#editOrderDiv");
		
		$editOrderDiv.show();
		$("#addOrder").show();
	
		$addOrderDiv.hide();

	}
	
	
	
	
	
	
	//RESTORANI APPENDOVANJE TABELE
	var $restautantTable = $("#restautantTable");
	
	function addRestaurant(restoran)
	{
		
		//DODAVANJE U COMBOBOX ZA ARTIKLE
		$("#artikalRestoranSelect").append('<option value="'+ restoran.id +'">' + restoran.ime + '</option>');
		$("#newArtiklRestoranSelect").append('<option value="'+ restoran.id +'">' + restoran.ime + '</option>');
		
		$restautantTable.append('<tr id="restoran' + restoran.id + '"> <td>' + restoran_cnt + '</td> <td>' + restoran.ime + '</td> <td>' + restoran.tip_str + '</td> <tr>');
		restoran_cnt++;
		
		
		//RESTORAN SELEKTOVANJE ELEMENTA U TABELI
		$("#restoran" + restoran.id).click(function (){	
			var $clicked_tr = $(this);
			
			$("tr").css("background-color","white");
			$clicked_tr.css( "background-color", "#eee" );		
			
			restoranEditShow();
			InsertRestaurantIntoEditBox(restoran);
			
		});
	}
	
	//ARTIKLI APPENDOVANJE TABELE
	var $artikalTable = $("#artikalTable");
	function addArtikal(artikal)
	{
		//UBACIVANJE SVIH ARTIKALA U LISTU ZA IZMENU ORDERA
		$("#artikal").append('<option id="' + artikal.id + '">' + artikal.ime + " - " + artikal.restoran_maticni + '</option>');
		$("#newArtikal").append('<option id="' + artikal.id + '">' + artikal.ime + " - " + artikal.restoran_maticni + '</option>');
		
		
		$artikalTable.append('<tr id="artikal' + artikal.id + '"> <td>' + artikal_cnt + '</td> <td>' + artikal.ime + '</td> <td>' + artikal.tip + '</td> <tr>');
		artikal_cnt++;
		
		//ARTIKAL SELEKTOVANJE ELEMENTA U TABLEI
		$("#artikal" + artikal.id).click(function (){
			var $clicked_tr = $(this);
			
			$("tr").css("background-color","white");
			$clicked_tr.css( "background-color", "#eee" );	
			
			artikalEditShow();
			InsertArtikalIntoEditBox(artikal);
			
		});
	}
	
	//VOZILA APPENDOVANJE TABELE
	var $vozilaTable = $("#vozilaTable");
	function addVozilo(vozilo)
	{
		var print_type = 'a';
		
		if(vozilo.type == 0)
			print_type = "Bicycle";
		if(vozilo.type == 1)
			print_type = "Scooter";
		if(vozilo.type == 2)
			print_type = "Car";
		
		$vozilaTable.append('<tr id="vozilo' + vozilo.id + '"> <td>' + vozilo_cnt + '</td> <td>' + vozilo.marka + " " + vozilo.model + '</td> <td>' + print_type + '<tr>');
		vozilo_cnt++;
		
		//ARTIKAL SELEKTOVANJE ELEMENTA U TABLEI
		$("#vozilo" + vozilo.id).click(function (){
			var $clicked_tr = $(this);
			
			$("tr").css("background-color","white");
			$clicked_tr.css( "background-color", "#eee" );	
			
			voziloEditShow();
			InsertVoziloIntoEditBox(vozilo);
			
		});
	}
	//USER APPENDOVANJE TABELE **********************
	var $userTable = $("#userTable");
	function addUser(user)
	{
		var print_type;
		
		if(user.user_type == 0)
			print_type = "Customer";
		if(user.user_type == 1)
			print_type = "Delivery";
		if(user.user_type == 2)
			print_type = "Admin"; 
		
		$userTable.append('<tr id="user' + user.id + '"> <td>' + user_cnt + '</td> <td>' + user.username + '</td> <td>' + print_type + '<tr>');
		user_cnt++;
		
		$("#user" + user.id).click(function (){
			var $clicked_tr = $(this);
			
			$("tr").css("background-color","white");
			$clicked_tr.css( "background-color", "#eee" );	
			InsertUserIntoEditBox(user);	
		});
	}
	
	//DELIVERY GUY APPENDOVANJE SELECT LISTE 
	function addDeliveryGuyToList(delivery)
	{
	
		
		if(delivery.voziloId == -1)
		{
			$("#orderDelivererSelect").append('<option value="' + delivery.id + '">' + delivery.username + '</option>');
			$("#newOrderDelivererSelect").append('<option value="' + delivery.id + '">' + delivery.username + '</option>');
		}

	}
	
	function addUserToList(user)
	{
		$("#newOrderUserSelect").append('<option value="' + user.id + '">' + user.username + '</option>');
	}
	
	//SVA NEZAUZETA VOZILA IDU U LISTU 
	function addVoziloToList(vozilo)
	{
		if(vozilo.zauzeto == 0)
		{
			$("#orderVoziloSelect").append('<option value="' + vozilo.id + '">' + vozilo.marka + " " + vozilo.model + '</option>');
			$("#newOrderVehicleSelect").append('<option value="' + vozilo.id + '">' + vozilo.marka + " " + vozilo.model + '</option>');
		}
	}
	
	//ORDER APPENDOVANJE TABELE **************************
	
	var $ordersTable = $("#ordersTable");
	function addOrder(order)
	{
		var print_type;
		
		if(order.stanje == 0)
			print_type = "Ordered";
		if(order.stanje == 1)
			print_type = "Delivering";
		if(order.stanje == 2)
			print_type = "Delivered";
		if(order.stanje == 3)
			print_type = "Cancled"; 
		
		$ordersTable.append('<tr id="order' + order.id + '"> <td>' + order_cnt + '</td> <td>' + order.dateTime_order + '</td> <td>' + print_type + '<tr>');
		order_cnt++;
		
		$("#order" + order.id).click(function (){
			var $clicked_tr = $(this);
			
			
			
			$("tr").css("background-color","white");
			$clicked_tr.css( "background-color", "#eee" );	
			
			orderEditShow();
			InsertOrderIntoEditBox(order);	
			
			
		});
	}
	
	
	
	
	
	
	
	
	
	
	//IZMENA RESTORANA************************************************************
	$("#editRestaurant").click(function () {
		
		//GETTOVANJE VR
		var restoranId = $("#selektovanRestoranId").val();
		var restoranIme = $("#restoranImeInput").val();
		var restoranUlica = $("#restoranUlicaInput").val();
		var restoranBroj = $("#restoranBrojInput").val();
		var restoranTip = $("#restoranTipSelect").val();
		
		var $restoranId = $("#selektovanRestoranId");
		var $restoranIme = $("#restoranImeInput");
		var $restoranUlica = $("#restoranUlicaInput");
		var $restoranBroj = $("#restoranBrojInput");
		var $restoranTip = $("#restoranTipSelect");
		
		var ispravno1 = ValidirajIdRestorana(restoranId);
		if(ispravno1 == false) return; 
		var ispravno2 = ValidirajImeRestorana(restoranIme,$restoranIme);
		var ispravno3 = ValidirajUlicuRestorana(restoranUlica,$restoranUlica);
		var ispravno4 = ValidirajBrojRestorana(restoranBroj,$restoranBroj);
		var ispravno5 = ValidirajCombobox(restoranTip, $restoranTip);
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true && ispravno5 == true)
		{
			$.ajax({
				type: 'PUT',
				url: "rest/restorani/modify",
				contentType: 'application/json',
				data: JSON.stringify({id: restoranId, ime: restoranIme, ulica: restoranUlica, broj_ulice: restoranBroj, tip_str: restoranTip}),
				dataType:"json",
				complete: function(data)
				{
					if(data.responseJSON != null)
					{
						console.log("izmenjen");
						
						var $target_restoran = $("#restoran" + restoranId);
						$target_restoran.find("td:nth-last-child(1)").remove();
						$target_restoran.find("td:nth-last-child(1)").remove();
						$target_restoran.append("<td>" + restoranIme + "</td>");
						$target_restoran.append("<td>" + restoranTip + "</td>");
						
						$("#restoran" + restoranId).click(function (){	
						var $clicked_tr = $(this);
						
						$("tr").css("background-color","white");
						$clicked_tr.css( "background-color", "#eee" );		
						
						restoranEditShow();
						InsertRestaurantIntoEditBox2(restoranId, restoranIme, restoranUlica, restoranBroj, restoranTip);
						
					});
				}
				}
			});
		}
	});
	
	
	
	//BRISANJE RESTORANA************************************************************
	$("#removeRestaurant").click(function () {
		
		var restoranId = $("#selektovanRestoranId").val();
		var ispravno1 = ValidirajIdRestorana(restoranId);
		
		if(ispravno1 == false)
		{
			return;
		}
		$.ajax({
			type: 'DELETE',
			url: "rest/restorani/delete/" + restoranId,
			success: function()
			{
				var $target_restoran = $("#restoran" + restoranId);
				$target_restoran.remove();
				clearEditSelection();
				alert("A restauran and all it's meals/drinks have been deleted.")
			}
		});
		
	});
	
	//DODAVANJE RESTORANA************************************************************
	$("#createRestaurant").click(function (){
		
			
		var $newRestoranImeInput = $("#newRestoranImeInput");
		var $newRestoranStreetSelect = $("#newRestoranStreetSelect");
		var $newRestoranBrojInput = $("#newRestoranBrojInput");
		var $newRestoranTypeSelect = $("#newRestoranTypeSelect");
		
		var newRestoranImeInput = $("#newRestoranImeInput").val();
		var newRestoranStreetSelect = $("#newRestoranStreetSelect").val();
		var newRestoranBrojInput = $("#newRestoranBrojInput").val();
		var newRestoranTypeSelect = $("#newRestoranTypeSelect").val();
		
		
		var ispravno1 = ValidirajImeRestorana(newRestoranImeInput,$newRestoranImeInput);
		var ispravno2 = ValidirajUlicuRestorana(newRestoranStreetSelect, $newRestoranStreetSelect);
		var ispravno3 = ValidirajBrojRestorana(newRestoranBrojInput,$newRestoranBrojInput);
		var ispravno4 = ValidirajCombobox(newRestoranTypeSelect, $newRestoranTypeSelect);
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true)
		{
			$.ajax({
				type: 'POST',
				url: "rest/restorani/add",
				contentType: 'application/json',
				data: JSON.stringify({ime: newRestoranImeInput, ulica: newRestoranStreetSelect, broj_ulice: newRestoranBrojInput, tip_str: newRestoranTypeSelect}),
				dataType:"json",
				complete: function(data)
				{
					var newRestoran = data.responseJSON;
					if(newRestoran != null)
					{
						console.log(newRestoran);
						addRestaurant(newRestoran);
						
					}	
					
				}
			});
		}	
	});
	
	
	//ARTIKAL EDIT************************************************************
	$("#editArtikal").click(function () {
		
		//GETTOVANJE VR
		var selektovanArtikalId = $("#selektovanArtikalId").val();
		var artikalImeInput = $("#artikalImeInput").val();
		var artikalRestoranSelect = $("#artikalRestoranSelect").val();
		var artikalCenaInput = $("#artikalCenaInput").val();
		var artikalTipSelect = $("#artikalTipSelect").val();
		var artikalGramazaInput = $("#artikalGramazaInput").val();
		
		var $selektovanArtikalId = $("#selektovanArtikalId");
		var $artikalImeInput = $("#artikalImeInput");
		var $artikalRestoranSelect = $("#artikalRestoranSelect");
		var $artikalCenaInput = $("#artikalCenaInput");
		var $artikalTipSelect = $("#artikalTipSelect");
		var $artikalGramazaInput = $("#artikalGramazaInput");
		
		//VALIDACIJE
		var ispravno1 = ValidirajArtikalID(selektovanArtikalId);
		if(ispravno1 == false) return; 
		//ISTE SU VALIDACIJA PA REKO NEKA
		var ispravno2 = ValidirajImeRestorana(artikalImeInput,$artikalImeInput);
		var ispravno3 = ValidirajCombobox(artikalRestoranSelect,$artikalRestoranSelect);
		var ispravno4 = ValidirajBrojRestorana(artikalCenaInput,$artikalCenaInput);
		var ispravno5 = ValidirajCombobox(artikalTipSelect, $artikalTipSelect);
		var ispravno6 = ValidirajBrojRestorana(artikalGramazaInput,$artikalGramazaInput);
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true && ispravno5 == true)
		{
			$.ajax({
				type: 'PUT',
				url: "rest/artikli/modify",
				contentType: 'application/json',
				data: JSON.stringify({id: selektovanArtikalId, id_restorana: artikalRestoranSelect, ime: artikalImeInput, cena: artikalCenaInput, tip: artikalTipSelect, gramaza: artikalGramazaInput}),
				dataType:"json",
				complete: function(data)
				{
					
					if(data.responseJSON != null)
					{
						console.log("izmenjen");
						
						var $target_artikal = $("#artikal" + selektovanArtikalId);
						$target_artikal.find("td:nth-last-child(1)").remove();
						$target_artikal.find("td:nth-last-child(1)").remove();
						$target_artikal.append("<td>" + artikalImeInput + "</td>");
						$target_artikal.append("<td>" + artikalTipSelect + "</td>");
						
						$("#artikal" + selektovanArtikalId).click(function (){
							var $clicked_tr = $(this);
							
							$("tr").css("background-color","white");
							$clicked_tr.css( "background-color", "#eee" );	
							
							artikalEditShow();
							InsertArtikalIntoEditBox2(selektovanArtikalId, artikalRestoranSelect, artikalImeInput, artikalCenaInput, artikalTipSelect, artikalGramazaInput);
							
						 });
					}
				}
			});
		}
	});

	//ARTIKAL BRISANJE ************************************************
	$("#removeArtikal").click(function (){
		var selektovanArtikalId = $("#selektovanArtikalId").val();
		var ispravno1 = ValidirajArtikalID(selektovanArtikalId);
		
		if(ispravno1 == false)
		{
			return;
		}
		
		$.ajax({
			type: 'DELETE',
			url: "rest/artikli/delete/" + selektovanArtikalId,
			success: function()
			{
				var $target_artikal = $("#artikal" + selektovanArtikalId);
				$target_artikal.remove();
				clearEditSelection();
			}
		});
		
	});
	//ARTIKAL DODAVANJE*************************************************
	$("#createArtikal").click(function (){
		
		
		var $newArtikalImeInput = $("#newArtikalImeInput");
		var $newArtiklRestoranSelect = $("#newArtiklRestoranSelect");
		var $newArtikalCenaInput = $("#newArtikalCenaInput")
		var $newArtikalGramazaInput = $("#newArtikalGramazaInput")
		var $newArtikalTipSelect = $("#newArtikalTipSelect")
		
		var newArtikalImeInput = $("#newArtikalImeInput").val();
		var newArtiklRestoranSelect = $("#newArtiklRestoranSelect").val();		
		var newArtikalCenaInput = $("#newArtikalCenaInput").val();
		var newArtikalGramazaInput = $("#newArtikalGramazaInput").val();
		var newArtikalTipSelect = $("#newArtikalTipSelect").val();
		
		var ispravno1 = ValidirajImeRestorana(newArtikalImeInput,$newArtikalImeInput);
		var ispravno4 = ValidirajCombobox(newArtiklRestoranSelect, $newArtiklRestoranSelect);
		var ispravno2 = ValidirajBrojRestorana(newArtikalCenaInput, $newArtikalCenaInput);
		var ispravno3 = ValidirajBrojRestorana(newArtikalGramazaInput, $newArtikalGramazaInput);
		var ispravno5 = ValidirajCombobox(newArtikalTipSelect,$newArtikalTipSelect);
		
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true && ispravno5 == true)
		{
			$.ajax({
				type: 'POST',
				url: "rest/artikli/add",
				contentType: 'application/json',
				data: JSON.stringify({id_restorana: newArtiklRestoranSelect, ime: newArtikalImeInput, cena: newArtikalCenaInput, tip: newArtikalTipSelect, gramaza: newArtikalGramazaInput}),
				dataType:"json",
				complete: function(data)
				{
					var newArtikal = data.responseJSON;
					console.log(newArtikal);
					addArtikal(newArtikal);
				}
			});
		}
	});
	
	
	
	
	//***********VOZILO IZMENA************************************
	$("#editVozilo").click(function () {
		
		//GETTOVANJE VR
		var selektovanoVoziloId = $("#selektovanoVoziloId").val();
		var VoziloBrandInput = $("#VoziloBrandInput").val();
		var VoziloModelInput = $("#VoziloModelInput").val();
		var VoziloTypeSelect = $("#VoziloTypeSelect").val();
		var VoziloPlateInput = $("#VoziloPlateInput").val();
		var VoziloYearInput = $("#VoziloYearInput").val();
		var VoziloNoteArea = $("#VoziloNoteArea").val();
		
		var $selektovanoVoziloId = $("#selektovanoVoziloId");
		var $VoziloBrandInput = $("#VoziloBrandInput");
		var $VoziloModelInput = $("#VoziloModelInput");
		var $VoziloTypeSelect = $("#VoziloTypeSelect");
		var $VoziloPlateInput = $("#VoziloPlateInput");
		var $VoziloYearInput = $("#VoziloYearInput");
		var $VoziloNoteArea = $("#VoziloNoteArea");
		
		
		//VALIDACIJE
		var ispravno1 = ValidirajVoziloId(selektovanoVoziloId);
		if(ispravno1 == false) return; 
		//ISTE SU VALIDACIJA PA REKO NEKA
		
		var ispravno2 = ValidirajImeRestorana(VoziloBrandInput,$VoziloBrandInput);
		var ispravno3 = ValidirajImeRestorana(VoziloModelInput,$VoziloModelInput);
		var ispravno4 = ValidirajCombobox(VoziloTypeSelect,$VoziloTypeSelect);
		var ispravno5 = ValidirajImeRestorana(VoziloPlateInput, $VoziloPlateInput);
		var ispravno6 = ValidirajGodinuProizvodnje(VoziloYearInput,$VoziloYearInput);
		//za note ne treba validacija
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true && ispravno5 == true && ispravno6 == true)
		{
			
			console.log("idemo sin");
			
			$.ajax({
				type: 'PUT',
				url: "rest/vozila/modify",
				contentType: 'application/json',
				data: JSON.stringify({id: selektovanoVoziloId, marka: VoziloBrandInput, model: VoziloModelInput, type: VoziloTypeSelect, registracija: VoziloPlateInput, godinaProzivodnje: VoziloYearInput, napomena: VoziloNoteArea}),
				dataType:"json",
				complete: function(data)
				{
					console.log(VoziloNoteArea);
					
					var newVozilo = data.responseJSON;
					if(newVozilo != null)
					{	
						var $target_artikal = $("#vozilo" + selektovanoVoziloId);
						$target_artikal.find("td:nth-last-child(1)").remove();
						$target_artikal.find("td:nth-last-child(1)").remove();
						$target_artikal.append("<td>" + newVozilo.marka + " " + newVozilo.model + "</td>");
						
						if(newVozilo.type == 0)
							$target_artikal.append("<td>" + 'Bicycle' + "</td>");
						if(newVozilo.type == 1)
							$target_artikal.append("<td>" + 'Scooter' + "</td>");
						if(newVozilo.type == 2)
							$target_artikal.append("<td>" + 'Car' + "</td>");
						
						$("#vozilo" + selektovanoVoziloId).click(function (){
							var $clicked_tr = $(this);
							
							$("tr").css("background-color","white");
							$clicked_tr.css( "background-color", "#eee" );	
							
							voziloEditShow();
							InsertVoziloIntoEditBox(newVozilo);
						});		
					}
				}
			});
		}
	});

	//*****VOZILO BRISANJE************************************************
	
	$("#removeVozilo").click(function () {
		var selektovanoVoziloId = $("#selektovanoVoziloId").val();
		console.log(selektovanoVoziloId);
		var ispravno1 = ValidirajVoziloId(selektovanoVoziloId);
		
		if(ispravno1 == false)
		{
			return;
		}
		
		
		$.ajax({
			type: 'GET',
			url: 'rest/vozila/deleteValidation/' + selektovanoVoziloId,
			complete: function(data)
			{
				//provera da li brisano vozilo obavlja neku narudzbiunu
				if(data.responseText!="")
				{
					$.ajax({
						type: 'DELETE',
						url: "rest/vozila/delete/" + selektovanoVoziloId,
						success: function()
						{
							var $target_vozilo = $("#vozilo" + selektovanoVoziloId);
							$target_vozilo.remove();
							clearEditSelection();
						}
					});
				}
				else
				{
					alert("The selected vehicle is currently in usage, try deleting later.");
					return;
				}
			}
		});
	
		
	});
	
	//VOZILO DODAVANJE ******************************************************
	$("#createVozilo").click(function (){
		
		
		var $newVoziloBrandInput = $("#newVoziloBrandInput");
		var $newVoziloModelInput = $("#newVoziloModelInput");
		var $newVoziloTypeSelect = $("#newVoziloTypeSelect");
		var $newVoziloPlateInput = $("#newVoziloPlateInput");
		var $newVoziloYearInput = $("#newVoziloYearInput");
		var $newVoziloNoteArea = $("#newVoziloNoteArea");
		
		var newVoziloBrandInput = $("#newVoziloBrandInput").val();
		var newVoziloModelInput = $("#newVoziloModelInput").val();
		var newVoziloTypeSelect = $("#newVoziloTypeSelect").val();
		var newVoziloPlateInput = $("#newVoziloPlateInput").val();
		var newVoziloYearInput = $("#newVoziloYearInput").val();
		var newVoziloNoteArea = $("#newVoziloNoteArea").val();
		
		var ispravno1 = ValidirajImeRestorana(newVoziloBrandInput,$newVoziloBrandInput);
		var ispravno2 = ValidirajImeRestorana(newVoziloModelInput,$newVoziloModelInput);
		var ispravno3 = ValidirajCombobox(newVoziloTypeSelect,$newVoziloTypeSelect);
		var ispravno4 = ValidirajImeRestorana(newVoziloPlateInput, $newVoziloPlateInput);
		var ispravno5 = ValidirajGodinuProizvodnje(newVoziloYearInput,$newVoziloYearInput);
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true && ispravno4 == true && ispravno5 == true)
		{
			$.ajax({
				type: 'POST',
				url: "rest/vozila/add",
				contentType: 'application/json',
				data: JSON.stringify({marka: newVoziloBrandInput, model: newVoziloModelInput, type: newVoziloTypeSelect, registracija: newVoziloPlateInput, godinaProzivodnje: newVoziloYearInput, napomena: newVoziloNoteArea}),
				dataType:"json",
				complete: function(data)
				{
					var newVozilo = data.responseJSON;
					if(newVozilo != null)
					{
						console.log(newVozilo);
						addVozilo(newVozilo);
					}
				}
			});
		}
	});
	
	
	//*****PROMENA TIPA KORISNIKA************************************
	$("#changeUserType").click(function(){
		
		var selektovanUser = $("#selektovanUser").val();
		var ispravno = ValidirajUserChangeType(selektovanUser);
		
		var usernameInput = $("#usernameInput").val();
		var passwordInput = $("#passwordInput").val();
		
		var $userTypeSelect = $("#userTypeSelect");
		var userTypeSelect = $("#userTypeSelect").val();
		
		if(ispravno == false)
		{
			return;
		}
		
		var ispravno2 = ValidirajCombobox(userTypeSelect, $userTypeSelect);
		
		if(ispravno2 == true)
		{
			$.ajax({
				type: 'PUT',
				url: 'rest/users/changeType/' + selektovanUser + "/" + userTypeSelect,
				complete: function(data)
				{
					var changed_user_type = data.responseText;
					
					var $target_user = $("#user" + selektovanUser);
					$target_user.find("td:nth-last-child(1)").remove();
					
					if(changed_user_type == 0)
						$target_user.append("<td>" + 'Customer' + "</td>");
					if(changed_user_type == 1)
						$target_user.append("<td>" + 'Delivery' + "</td>");
					if(changed_user_type == 2)
						$target_user.append("<td>" + 'Admin' + "</td>");
					
					$("#user" + selektovanUser).click(function (){
						var $clicked_tr = $(this);
						
						$("tr").css("background-color","white");
						$clicked_tr.css( "background-color", "#eee" );	
						
						InsertUserIntoEditBox2(selektovanUser, usernameInput, passwordInput, changed_user_type);
					});
					
					
				}
			});	
		}
		
	});
	
	
	
	
	
	
	
	
	//**************ORDER************************************
	
	
	//**********DODAVANJE U LISTU ARTIKALA
	$("#addArtikalToOrder").click(function () {
		
		
		var selektovanOrderId = $("#selektovanOrderId").val();
		
		if(selektovanOrderId == "")
			{
				alert("Select an order first.");
				return;
			}
		
		
		var $select_artikal_comobobox = $("#artikal option:selected");
		var selektovan_artikal_id = $select_artikal_comobobox.attr("id");
		var ispravno1 = ValidirajCombobox(selektovan_artikal_id, $select_artikal_comobobox);
		
		var $artikalKvantitet = $("#artikalKvantitet");
		var artikalKvantitet = $artikalKvantitet.val();
		var ispravno2 = ValidirajBrojRestorana(artikalKvantitet, $artikalKvantitet);
		
		
		if(ispravno1 == true && ispravno2 == true)
		{
			
			$.ajax({
				
				type: 'GET',
				url: 'rest/artikli/getArtikalById/' + selektovan_artikal_id,
				complete:function(data)
				{
					
					
					
					var response_artikal = data.responseJSON;
					
					console.log("artikal br: " + selektovan_artikal_id);
					
					$("#artikliList").append('<option id="' + response_artikal.id + "-" + artikalKvantitet + '" >' + artikalKvantitet + ' x ' + response_artikal.ime + ' from ' + response_artikal.restoran_maticni + '</option>');
				}
				
			});
			
		}
		
	});
	
	
	//**********DODAVANJE ARTIKLA U LISTU NOVOG ORDERA
	$("#newAddArtikal").click(function () {
		

		
		
		var $select_artikal_comobobox = $("#newArtikal option:selected");
		
		
		
		var selektovan_artikal_id = $select_artikal_comobobox.attr("id");
		var ispravno1 = ValidirajCombobox(selektovan_artikal_id, $select_artikal_comobobox);
		
		var $artikalKvantitet = $("#newArtikalKvantitet");
		var artikalKvantitet = $artikalKvantitet.val();
		var ispravno2 = ValidirajBrojRestorana(artikalKvantitet, $artikalKvantitet);
		
		
		if(ispravno1 == true && ispravno2 == true)
		{
			
			$.ajax({
				
				type: 'GET',
				url: 'rest/artikli/getArtikalById/' + selektovan_artikal_id,
				complete:function(data)
				{
	
					var response_artikal = data.responseJSON;
					
					console.log("artikal br: " + selektovan_artikal_id);
					
					$("#newArtikliList").append('<option id="' + response_artikal.id + "-" + artikalKvantitet + '" >' + artikalKvantitet + ' x ' + response_artikal.ime + ' from ' + response_artikal.restoran_maticni + '</option>');
				}
				
			});
			
		}
		
	});
	
	
	
	
	
	
	//**********BRISANJE ARTIKLA IZ LISTE ARTIKALA
	
	
	
	$("#removeArtikalFromOrder").click(function () {
		
		
		var selektovanOrderId = $("#artikliList option:selected");
		
		if(selektovanOrderId.attr("id") == undefined)
			return;
		
		console.log("selektovan: " + selektovanOrderId.attr("id"));
		
		selektovanOrderId.remove();

	});
	
	
	
	
	//**********BRISANJE ARTIKLA IZ LISTE ARTIKALA NOVOG ORDERA
	
	
	
	$("#newRemoveArtikal").click(function () {
		
		
		var selektovanOrderId = $("#newArtikliList option:selected");
		
		if(selektovanOrderId.attr("id") == undefined)
			return;
		
		console.log("selektovan: " + selektovanOrderId.attr("id"));
		
		selektovanOrderId.remove();

	});
	
	
	//****************ORDER EDIT**********************
	
	$("#editOrder").click(function () {
		
		
		var selektovanOrderId = $("#selektovanOrderId").val();

		
		var $orderStatusSelect = $("#orderStatusSelect");
		var orderStatusSelect = $orderStatusSelect.val();
		var orderNote = $("#orderNote").val();

		
		var $orderDelivererSelect = $("#orderDelivererSelect");
		var $orderVoziloSelect = $("#orderVoziloSelect");
		var orderDelivererSelect = 0;
		var orderVoziloSelect = 0;
		
		SkupiSveOrdere();
		
		if(selektovanOrderId == "")
		{
			alert("Select an order first.");
			return;
		}
		if(sakupljeni_string == "")
		{
			alert("Order cannot be empty.");
			return;
		}
		

		
		
		console.log(sakupljeni_string);
		
		var ispravno = ValidirajCombobox(orderStatusSelect, $orderStatusSelect);
		var ispravno1 = true;
		var ispravno2 = true;
		if(orderStatusSelect == 1)
		{
			orderDelivererSelect = $("#orderDelivererSelect").val();
			orderVoziloSelect = $("#orderVoziloSelect").val();	
			
			ispravno1 = ValidirajCombobox(orderDelivererSelect, $orderDelivererSelect);
			ispravno2 = ValidirajCombobox(orderVoziloSelect, $orderVoziloSelect);
			
			console.log(orderDelivererSelect + " ; " + orderVoziloSelect);			
			
			
		}
		
		if(ispravno == true && ispravno1 == true && ispravno2 == true)
		{
			$.ajax({
				
				type: 'PUT',
				url: "rest/orders/modify",
				contentType: 'application/json',
				data: JSON.stringify({id: selektovanOrderId, stanje: orderStatusSelect, artikli: sakupljeni_string,napomena: orderNote, dostavljac_id: orderDelivererSelect, vozilo_id: orderVoziloSelect}),
				dataType:"json",
				complete: function(data)
				{
					
					var newOrder = data.responseJSON;
					
					console.log(newOrder);
				
					InsertOrderIntoEditBox
					
					
					var $target_order = $("#order" + selektovanOrderId);
					$target_order.find("td:nth-last-child(1)").remove();
					
					if(newOrder.stanje == 0)
						$target_order.append("<td>" + 'Ordered' + "</td>");
					if(newOrder.stanje == 1)
						$target_order.append("<td>" + 'Delivering' + "</td>");
					if(newOrder.stanje == 2)
						$target_order.append("<td>" + 'Delivered' + "</td>");
					if(newOrder.stanje == 3)
						$target_order.append("<td>" + 'Cancled' + "</td>");
					
					if(newOrder.stanje == 1)
					{
						$('#orderDelivererSelect[value="' + orderDelivererSelect + '"]').remove();	
						 $('#orderVoziloSelect[value="' + orderVoziloSelect + '"]').remove();	
						
						 $('#newOrderDelivererSelect[value="' + orderDelivererSelect + '"]').remove();	
						 $('#newOrderVehicleSelect[value="' + orderVoziloSelect + '"]').remove();
						 
						 $("#samoZaDelivering").hide();
					}
					
					$("#order" + selektovanOrderId).click(function (){
						var $clicked_tr = $(this);
						
						$("tr").css("background-color","white");
						$clicked_tr.css( "background-color", "#eee" );	
						
						InsertOrderIntoEditBox(newOrder);
					});
					
					
				}
				
			});
		}
		
	});
	
	
	//******************DODAVNAJE NOVOG ORDERA************************************
	
	$("#createOrder").click(function () {
		
		var orderNote = $("#orderNote").val();

		
		var $newOrderDelivererSelect = $("#newOrderDelivererSelect");
		var $newOrderVehicleSelect = $("#newOrderVehicleSelect");
		var $newOrderUserSelect = $("#newOrderUserSelect");
		
		var newOrderDelivererSelect = $newOrderDelivererSelect.val();
		var newOrderVehicleSelect = $newOrderVehicleSelect.val();
		var newOrderUserSelect = $newOrderUserSelect.val();
		
		SkupiSveOrdereNew();
		
		if(sakupljeni_string == "")
		{
			alert("Order cannot be empty.");
			return;
		}
		
		var ispravno1,ispravno2,ispravno3;
		ispravno1 = ValidirajCombobox(newOrderDelivererSelect, $newOrderDelivererSelect);
		ispravno2 = ValidirajCombobox(newOrderVehicleSelect, $newOrderVehicleSelect);
		ispravno3 = ValidirajCombobox(newOrderUserSelect, $newOrderUserSelect);
		
		console.log("SALJE SE ZAHTEV: " + newOrderDelivererSelect + newOrderVehicleSelect + newOrderUserSelect);
		
		if(ispravno1 == true && ispravno2 == true && ispravno3 == true)
		{
				$.ajax({
				
					type: 'POST',
					url: "rest/orders/adminAdd",
					contentType: 'application/json',
					data: JSON.stringify({artikli: sakupljeni_string,napomena: orderNote, dostavljac_id: newOrderDelivererSelect, vozilo_id: newOrderVehicleSelect, user_id: newOrderUserSelect}),
					dataType:"json",
					complete: function(data)
					{
						
						console.log(data.responseJSON);
						addOrder(data.responseJSON);
					
					}
				});
		}
		
	});
	
	
	//****BRISANJE ORDERA************
	$("#removeOrder").click(function () {
		var selektovanOrderId = $("#selektovanOrderId").val();
		
		if(selektovanOrderId == "")
		{
			alert("Select an order first.");
			return;
		}
		
		
		$.ajax({
			type: 'GET',
			url: 'rest/orders/deleteValidation/' + selektovanOrderId,
			complete: function(data)
			{
				//provera da li brisano vozilo obavlja neku narudzbiunu
				if(data.responseText!="")
				{
					$.ajax({
						type: 'DELETE',
						url: "rest/orders/delete/" + selektovanOrderId,
						success: function()
						{
							var $target_order = $("#order" + selektovanOrderId);
							$target_order.remove();
							clearEditSelection();
						}
					});
				}
				else
				{
					alert("The selected order is currently in delivery.");
					return;
				}
			}
		});
	
		
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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



//************VALIDACIJE*************************

//RESTORAN EDIT

function ValidirajIdRestorana(id)
{
	if(id == "")
	{
		alert("Select a restaurant first.")
		return false;
	}
	else
	{
		return true;
	}
}




function ValidirajImeRestorana(restoranIme, $domEl)
{
	console.log(restoranIme);
	console.log($domEl.val());
	
	if (/[^0-9a-zA-Z\s'šđžčćŠĐŽČĆ-]/gi.test(restoranIme) || restoranIme.length==0)
	{
		$domEl.css("background-color","#ff9db3");
		$domEl.attr("placeholder", "Only letters and numbers!");
		$domEl.val("");
		console.log("ne valja ime");
		return false;
	}

	return true;
}

function ValidirajUlicuRestorana(restoranUlica,$domEl)
{
	
	if (/[^a-zA-Z\s'šđžčćŠĐŽČĆ]/gi.test(restoranUlica) || restoranUlica.length==0)
	{
		$domEl.css("background-color","#ff9db3");
		$domEl.attr("placeholder", "Only letters!");
		$domEl.val("");
		console.log("ne valja ulica");

		return false;
	}
	return true;
}
function ValidirajBrojRestorana(restoranBroj,$domEl)
{
	
	if (/[^0-9]/gi.test(restoranBroj) || restoranBroj.length==0)
	{
		$domEl.css("background-color","#ff9db3");
		$domEl.attr("placeholder", "Only numbers!");
		$domEl.val("");
		console.log("ne valja broj");
		return false;
	}
	return true;
}
function ValidirajGodinuProizvodnje(godina,$domEl)
{
	if (/[^0-9]/gi.test(godina) || godina.length==0 || godina < 1950)
	{
		$domEl.css("background-color","#ff9db3");
		$domEl.attr("placeholder", "Insert a year after 1950!");
		$domEl.val("");
		console.log("ne valja broj");
		return false;
	}
	return true;
}

function ValidirajCombobox(restoranTip, $domEl)
{
	console.log(restoranTip);
	if(restoranTip == null)
	{
		$domEl.css("background-color","#ff9db3");
		return false;
	}
	else
		return true;
}

//VALIDIRANJE ARTIKLA
function ValidirajArtikalID(id)
{
	if(id == "")
	{
		alert("Select a meal or drink first.")
		return false;
	}
	else
	{
		return true;
	}
}

function ValidirajVoziloId(id)
{
	if(id == "")
	{
		alert("Select a vehicle first.")
		return false;
	}
	else
	{
		return true;
	}
}
function ValidirajUserChangeType(selekvotanUser)
{
	if(selekvotanUser == "")
	{
		alert("Select an user first.");
		return false;
	}	
	else
	{
		return true;
	}
}

function InsertRestaurantIntoEditBox(restoran)
{
	var $restoranImeInput = $("#restoranImeInput");
	var $restoranUlicaInput = $("#restoranUlicaInput");
	var $restoranBrojInput = $("#restoranBrojInput");
	var $restoranTipSelect = $("#restoranTipSelect");
	var $restoranArtikli = $("#restoranArtikli");
	var $selektovanRestoranId = $("#selektovanRestoranId");
	$restoranArtikli.empty();
	
	$selektovanRestoranId.val(restoran.id);
	$restoranImeInput.val(restoran.ime);
	$restoranUlicaInput.val(restoran.ulica);
	$restoranBrojInput.val(restoran.broj_ulice);
	$restoranTipSelect.val(restoran.tip_str);
	$.each(restoran.artikli_restorana, function (i,artikal){
		$restoranArtikli.append("<option>" + artikal.ime + "</option>");
	});
}

function InsertRestaurantIntoEditBox2(restoranId, restoranIme, restoranUlica, restoranBroj, restoranTip)
{
	var $restoranImeInput = $("#restoranImeInput");
	var $restoranUlicaInput = $("#restoranUlicaInput");
	var $restoranBrojInput = $("#restoranBrojInput");
	var $restoranTipSelect = $("#restoranTipSelect");
	var $restoranArtikli = $("#restoranArtikli");
	var $selektovanRestoranId = $("#selektovanRestoranId");
	
	$selektovanRestoranId.val(restoranId);
	$restoranImeInput.val(restoranIme);
	$restoranUlicaInput.val(restoranUlica);
	$restoranBrojInput.val(restoranBroj);
	$restoranTipSelect.val(restoranTip);
}

function InsertArtikalIntoEditBox(artikal)
{
	var $artikalImeInput = $("#artikalImeInput");
	var $artikalRestoranSelect = $("#artikalRestoranSelect");
	var $artikalCenaInput = $("#artikalCenaInput");
	var $artikalGramazaInput = $("#artikalGramazaInput");
	var $artikalTipSelect = $("#artikalTipSelect");
	var $selektovanArtikalId = $("#selektovanArtikalId");
	
	$selektovanArtikalId.val(artikal.id);
	$artikalImeInput.val(artikal.ime);
	$artikalRestoranSelect.val(artikal.id_restorana);
	$artikalCenaInput.val(artikal.cena);
	$artikalGramazaInput.val(artikal.gramaza);
	$artikalTipSelect.val(artikal.tip);
}


function InsertArtikalIntoEditBox2(selektovanArtikalId, artikalRestoranSelect, artikalImeInput, artikalCenaInput, artikalTipSelect, artikalGramazaInput)
{
	var $artikalImeInput = $("#artikalImeInput");
	var $artikalRestoranSelect = $("#artikalRestoranSelect");
	var $artikalCenaInput = $("#artikalCenaInput");
	var $artikalGramazaInput = $("#artikalGramazaInput");
	var $artikalTipSelect = $("#artikalTipSelect");
	var $selektovanArtikalId = $("#selektovanArtikalId");
	
	$selektovanArtikalId.val(selektovanArtikalId);
	$artikalImeInput.val(artikalImeInput);
	$artikalRestoranSelect.val(artikalRestoranSelect);
	$artikalCenaInput.val(artikalCenaInput);
	$artikalGramazaInput.val(artikalGramazaInput);
	$artikalTipSelect.val(artikalTipSelect);
}

function InsertVoziloIntoEditBox(vozilo)
{
	var $VoziloBrandInput = $("#VoziloBrandInput");
	var $VoziloModelInput = $("#VoziloModelInput");
	var $VoziloTypeSelect = $("#VoziloTypeSelect");
	var $VoziloPlateInput = $("#VoziloPlateInput");
	var $VoziloYearInput = $("#VoziloYearInput");
	var $VoziloNoteArea = $("#VoziloNoteArea");
	var $selektovanoVoziloId = $("#selektovanoVoziloId");
	
	$selektovanoVoziloId.val(vozilo.id);
	$VoziloBrandInput.val(vozilo.marka);
	$VoziloModelInput.val(vozilo.model);
	$VoziloTypeSelect.val(vozilo.type);
	$VoziloPlateInput.val(vozilo.registracija);
	$VoziloYearInput.val(vozilo.godinaProzivodnje);
	$VoziloNoteArea.val(vozilo.napomena);
}

function InsertUserIntoEditBox(user)
{
	var $selektovanUser = $("#selektovanUser");
	var $usernameInput = $("#usernameInput");
	var $passwordInput = $("#passwordInput");
	var $userTypeSelect = $("#userTypeSelect");
	
	$selektovanUser.val(user.id);
	$usernameInput.val(user.username);
	$passwordInput.val(user.password);
	$userTypeSelect.val(user.user_type);
}
function InsertUserIntoEditBox2(userId, userName, userPass, userType)
{
	var $selektovanUser = $("#selektovanUser");
	var $usernameInput = $("#usernameInput");
	var $passwordInput = $("#passwordInput");
	var $userTypeSelect = $("#userTypeSelect");
	
	$selektovanUser.val(userId);
	$usernameInput.val(userName);
	$passwordInput.val(userPass);
	$userTypeSelect.val(userType);
}

function InsertOrderIntoEditBox(order)
{
	var $selektovanOrderId = $("#selektovanOrderId");
	var $orderStatusSelect = $("#orderStatusSelect");
	var $artikliList = $("#artikliList");
	var $orderNote = $("#orderNote");
	var $totalCostInput = $("#totalCostInput");
	
	$selektovanOrderId.val(order.id);
	console.log("id: " + order.id);
	$orderStatusSelect.val(order.stanje);
	$totalCostInput.val(Number(order.total_cena).toFixed(2));
	
	if(order.napomena != "null")
		$orderNote.val(order.napomena);
	else
		$orderNote.val("");
	$artikliList.empty();
	$.each(order.artikli_objs, function(i,artikal){
		//ID - ID ARTIKLA KOJI JE UBACEN; VALUE - BROJ KOLIKO 
		$artikliList.append('<option id="' + artikal.id + "-" + artikal.kolicina + '" >' + artikal.kolicina + ' x ' + artikal.ime + ' from ' + artikal.restoran_maticni + '</option>');
		
			
		});
}

function clearEditSelection()
{
	$("tr").css("background-color","white");
	
	var $artikalImeInput = $("#artikalImeInput");
	var $artikalRestoranSelect = $("#artikalRestoranSelect");
	var $artikalCenaInput = $("#artikalCenaInput");
	var $artikalGramazaInput = $("#artikalGramazaInput");
	var $artikalTipSelect = $("#artikalTipSelect");
	var $selektovanArtikalId = $("#selektovanArtikalId");
	
	$selektovanArtikalId.val("");
	$artikalImeInput.val("");
	$artikalRestoranSelect.val("");
	$artikalCenaInput.val("");
	$artikalGramazaInput.val("");
	$artikalTipSelect.val("");
	
	
	var $restoranImeInput = $("#restoranImeInput");
	var $restoranUlicaInput = $("#restoranUlicaInput");
	var $restoranBrojInput = $("#restoranBrojInput");
	var $restoranTipSelect = $("#restoranTipSelect");
	var $restoranArtikli = $("#restoranArtikli");
	var $selektovanRestoranId = $("#selektovanRestoranId");
	$restoranArtikli.empty();
	
	
	$restoranImeInput.val("");
	$restoranUlicaInput.val("");
	$restoranBrojInput.val("");
	$restoranTipSelect.val("");
	$selektovanRestoranId.val("");
	
	
	var $selektovanUser = $("#selektovanUser");
	var $usernameInput = $("#usernameInput");
	var $passwordInput = $("#passwordInput");
	var $userTypeSelect = $("#userTypeSelect");
	
	$selektovanUser.val("");
	$usernameInput.val("");
	$passwordInput.val("");
	$userTypeSelect.val("");
	
	var $VoziloBrandInput = $("#VoziloBrandInput");
	var $VoziloModelInput = $("#VoziloModelInput");
	var $VoziloTypeSelect = $("#VoziloTypeSelect");
	var $VoziloPlateInput = $("#VoziloPlateInput");
	var $VoziloYearInput = $("#VoziloYearInput");
	var $VoziloNoteArea = $("#VoziloNoteArea");
	var $selektovanoVoziloId = $("#selektovanoVoziloId");
	
	$selektovanoVoziloId.val("");
	$VoziloBrandInput.val("");
	$VoziloModelInput.val("");
	$VoziloTypeSelect.val("");
	$VoziloPlateInput.val("");
	$VoziloYearInput.val("");
	$VoziloNoteArea.val("");
	
	
	var $newOrderUserSelect = $("#newOrderUserSelect");
	var $newOrderDelivererSelect = $("#newOrderDelivererSelect");
	var $newOrderVehicleSelect = $("#newOrderVehicleSelect");
	var $newArtikal = $("#newArtikal");
	var $newOrderNote = $("#newOrderNote");
	var $newArtikalKvantitet = $("#newArtikalKvantitet");
	
	var $orderStatusSelect = $("#orderStatusSelect");
	var $artikal = $("#artikal");
	var $artikalKvantitet = $("#artikalKvantitet");
	var $orderNote = $("#orderNote");
	var $totalCostInput = $("#totalCostInput");
	
	$orderStatusSelect.val("");
	$artikal.val("");
	$artikalKvantitet.val("");
	$orderNote.val("");
	$totalCostInput.val("");
	$("#artikliList").empty();
}


function SkupiSveOrdere()
{
	var lista = $("#artikliList");
	
	sakupljeni_string = "";
	
	$("#artikliList option").each(function() 
		{
		
			var temp = $(this);
		
			var temp_id = temp.attr("id");
			
			sakupljeni_string = sakupljeni_string + temp_id + ";";
		
		}
	);

	
}


function SkupiSveOrdereNew()
{
	var lista = $("#newArtikliList");
	
	sakupljeni_string = "";
	
	$("#newArtikliList option").each(function() 
		{
		
			var temp = $(this);
		
			var temp_id = temp.attr("id");
			
			sakupljeni_string = sakupljeni_string + temp_id + ";";
		
		}
	);

	
}




