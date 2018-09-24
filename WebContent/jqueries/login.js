$(document).ready(function () {
	
	
	//ako logovan korisnik pokusa da pristupi ovoj stranici
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
				window.location.href = "index.html";
			}
			else
			{
				$.ajax({
					type: 'GET',
					url: 'rest/users/currentDeliveryGuy',
					contentType: 'application/json',
					dataType:"json",
					complete: function (data)
					{
						var loggedDeliveryGuy = data.responseJSON;
						if(loggedDeliveryGuy!=null)
						{
							window.location.href = "index.html";
						}
						else
						{
							$.ajax({
								type: 'GET',
								url: 'rest/users/currentAdmin',
								contentType: 'application/json',
								dataType:"json",
								complete: function (data)
								{
									var loggedAdmin = data.responseJSON;
									if(loggedAdmin !=null)
										{
											window.location.href = "index.html";
										}
								}
							});
						}
					}
				});
				
			}
		}
		  });
	
	
	

	//FUNKCIJA ZA AUTOMATSKU KAPITALIZACIJU IMENA I PREZIMENA
	$('#signUpName').keyup(function(event) {
	    var textBox = event.target;
	    var start = textBox.selectionStart;
	    var end = textBox.selectionEnd;
	    textBox.value = textBox.value.charAt(0).toUpperCase() + textBox.value.slice(1);
	    textBox.setSelectionRange(start, end);
	});

	$('#signUpLastname').keyup(function(event) {
	    var textBox = event.target;
	    var start = textBox.selectionStart;
	    var end = textBox.selectionEnd;
	    textBox.value = textBox.value.charAt(0).toUpperCase() + textBox.value.slice(1);
	    textBox.setSelectionRange(start, end);
	});
	
	
});


function login () 
{
	event.preventDefault();
	let username = $('#login_username').val();
	let password = $('#login_password').val();
	
	
	$.ajax({
		
		url: "rest/users/login",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({username: username, password: password}),
		dataType:"json",
		complete: function(data)
		{
			console.log(data.responseJSON);
			
			var loggedUser = data.responseJSON;
			
			//AKO NE POSTOJI USERNAME SA TIM IMENOM 
			if(loggedUser.username == 1)
			{
				console.log("salje zahtev za delivery guyja");

				//SALJI ZAHTEV DA VIDIS DA SE NIJE DOSTAVLJAC ULOGOVAO
				$.ajax({
					
					url: "rest/users/loginDeliveryGuy",
					type: 'POST',
					contentType: 'application/json',
					data: JSON.stringify({username: username, password: password}),
					dataType:"json",
					complete: function(data)
					{
						console.log(data.responseJSON);
						
						var loggedDelivery = data.responseJSON;
						
						//AKO NE POSTOJI USERNAME SA TIM IMENOM 
						if(loggedDelivery.username == 1)
						{
							
							//SALJEM ZAHTEV DA VIDIM DA SE NIJE ADMIN ULOGOVAO
							$.ajax({
								url: "rest/users/loginAdmin",
								type: 'POST',
								contentType: 'application/json',
								data: JSON.stringify({username: username, password: password}),
								dataType:"json",
								complete: function(data)
								{
									var loggedAdmin = data.responseJSON
									
									if(loggedAdmin.username == "1")
									{
										validirajUsername();

									}
									else if(loggedAdmin.password == "1")
									{
										validirajPassword();
									}
									else
									{
										window.location.href = "index.html";
									}
								}
							});	
							
						} else if(loggedDelivery.password == 1)
						{
							validirajPassword();
						} else //POSTOJI TAKAV KORISNIK - REDIREKCIJA
						{
							window.location.href = "index.html";
						}
						
					}
					
				});
								
			} else if(loggedUser.password == 1)
			{
				validirajPassword();
			} else //POSTOJI TAKAV KORISNIK - REDIREKCIJA 
			{
				window.location.href = "index.html";
			}
			
		}
		
	});
}





function signUp()
{
	event.preventDefault();
	let username = $('#signUpUsername').val();
	let password = $('#signUpPassword').val();
	
	let name = $('#signUpName').val();
	let lastname = $("#signUpLastname").val();
	
	let email = $("#signUpEmail").val();
	let phone = $("#signUpPhone").val();
	
	
	var dalie_ispravna = true;
	
	dalie_ispravna = usernameSignUpValidation(username);
	dalie_ispravna = passwordSignUpValidation(password);
	
	dalie_ispravna = nameSignUpValidation(name);
	dalie_ispravna = lastnameSignUpValidation(lastname);
	
	dalie_ispravna = emailSignUpValidation(email);
	dalie_ispravna = phoneSignUpValidation(phone);

	
	if(dalie_ispravna == true)
	{
		
		$.ajax({
			
			url: "rest/users/signUp",
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({username: username, password: password, name: name, lastname: lastname, tel: phone, email: email}),
			dataType:"json",
			complete: function(data)
			{				
				var loggedUser = data.responseJSON;
				
				if(loggedUser == null)
				{
					alert("Error while signing in.");
					location.refresh();
				}
				else
				{
					alert("You have successfully created an account." + "\n" + "Welcome " + loggedUser.username + " !");
					window.location.href = "index.html";
					location.refresh();

				}
				
			}
			
		});		
	}

}


//FUNKCIJA ZA VALIDACIJU USERNAME-A
function usernameSignUpValidation(username)
{
	
	
	
	//leksicka korektnost
	if (/[^0-9a-zA-ZšđžčćŠĐŽČĆ]/gi.test(username) || username.length==0)
	{
		
			var $signUpUsername = $('#signUpUsername');
			$signUpUsername.css({"background-color": "#ff9db3"});
	
			$signUpUsername.attr("placeholder", "Username can only contain letters and numbers!");
			$('#signUpUsername').val("");
			
			return false;
		
	}
	
	//jedinstvenost
	$.ajax({
		type: 'GET',
		url: 'rest/users/checkIfUnique/' + username,
		complete: function(data)
		{
			if(data.responseText=="")
			{
				var $signUpUsername = $('#signUpUsername');
				$signUpUsername.css({"background-color": "#ff9db3"});

				$signUpUsername.attr("placeholder", "This username is already taken!");
				$('#signUpUsername').val("");
				return false;
			}
		}
	});
	
	return true;
}
$ (function () {
	
		$("#signUpUsername").focusin(function() 
		{
			$(this).attr("placeholder", "");
			$(this).css({"background-color": "white"});
			
		});

});

//FUNKCIJA ZA VALIDACIJU PASSWORDA
function passwordSignUpValidation(password)
{
	
	if(/[^0-9a-zA-Z]/gi.test(password))
	{
		var $signUpPassword = $('#signUpPassword');
		$signUpPassword.css({"background-color": "#ff9db3"});

		$signUpPassword.attr("placeholder", "Password can only contain letters and numbers!");
		$('#signUpPassword').val("");
		return false;
	}
	
	if(password.length < 5)
	{
		var $signUpPassword = $('#signUpPassword');
		$signUpPassword.css({"background-color": "#ff9db3"});

		$signUpPassword.attr("placeholder", "Password must be over 5 charachters!");
		$('#signUpPassword').val("");
		return false;
	}
	return true;
}
$ (function () {
	
	$("#signUpPassword").focusin(function() 
	{
		$(this).attr("placeholder", "");
		$(this).css({"background-color": "white"});
		
	});

});


//FUNKCIJA ZA VALIDACIJU IMENA
function nameSignUpValidation(name)
{
	
	if(/[^a-zA-ZšđžčćŠĐŽČĆ]/gi.test(name) || name.length==0)
	{
		var $signUpName = $('#signUpName');
		$signUpName.css({"background-color": "#ff9db3"});

		$signUpName.attr("placeholder", "Name can only contain letters including šđžčćŠĐŽČĆ.");
		$('#signUpName').val("");
		return false;
	}
	
	return true;
}
$ (function () {
	
	$("#signUpName").focusin(function() 
	{
		$(this).attr("placeholder", "");
		$(this).css({"background-color": "white"});
		
	});

});


//FUNKCIJA ZA VALIDACIJU PREZIMENA
function lastnameSignUpValidation(lastname)
{
	
	if(/[^a-zA-ZšđžčćŠĐŽČĆ]/gi.test(lastname) || lastname.length==0)
	{
		var $signUpLastname = $('#signUpLastname');
		$signUpLastname.css({"background-color": "#ff9db3"});

		$signUpLastname.attr("placeholder", "Lastname can only contain letters including šđžčćŠĐŽČĆ. ");
		$('#signUpLastname').val("");
		return false;
	}
	
	return true;
}
$ (function () {
	
	$("#signUpLastname").focusin(function() 
	{
		$(this).attr("placeholder", "");
		$(this).css({"background-color": "white"});
		
	});

});

//FUNKCIJA ZA VALIDACIJU EMAIL-A
function emailSignUpValidation(email)
{
	
	if(/[^a-zA-Z0-9@\.]/gi.test(email) || email.length==0)
	{
		var $signUpEmail = $('#signUpEmail');
		$signUpEmail.css({"background-color": "#ff9db3"});

		$signUpEmail.attr("placeholder", "Wrong format. Example: coa123@coah.com");
		$('#signUpEmail').val("");
		return false;
	}
	
	$.ajax({
		type: 'GET',
		url: 'rest/users/checkIfUniqueEmail/' + email,
		complete: function(data)
		{
			if(data.responseText=="")
			{
				var $signUpEmail = $('#signUpEmail');
				$signUpEmail.css({"background-color": "#ff9db3"});

				$signUpEmail.attr("placeholder", "This email already exists in the database!");
				$('#signUpEmail').val("");
				return false;
			}
		}
	});
	
	return true;
}
$ (function () {
	
	$("#signUpEmail").focusin(function() 
	{
		$(this).attr("placeholder", "");
		$(this).css({"background-color": "white"});
		
	});

});

//FUNKCIJA ZA VALIDACIJU TELEFONA

function phoneSignUpValidation(phone)
{
	
	if(/[^0-9]/gi.test(phone) || phone.length==0)
	{
		var $signUpPhone = $('#signUpPhone');
		$signUpPhone.css({"background-color": "#ff9db3"});

		$signUpPhone.attr("placeholder", "Phone number must be a number!");
		$('#signUpPhone').val("");
		return false;
	}
	
	
	
	
	
	return true;
}
$ (function () {
	
	$("#signUpPhone").focusin(function() 
	{
		$(this).attr("placeholder", "");
		$(this).css({"background-color": "white"});
		
	});

});








//LOGIN DEO VALIDACIJE
function validirajUsername()
{
	var $usernameField = $('#login_username');
	$usernameField.css({"background-color": "#ff9db3"});

	$usernameField.attr("placeholder", "Wrong Username");
	$('#login_username').val("");
	

}

function validirajPassword()
{
	var $usernameField = $('#login_password');
	$usernameField.css({"background-color": "#ff9db3"});

	$usernameField.attr("placeholder", "Wrong Password");
	$('#login_password').val("");
	

}


$ (function () {
	
	$("#login_username").focusin(function() {
		
		$(this).css({"background-color": "white"});
		$(this).attr("placeholder", "Your Username");
		
	});

	$("#login_password").focusin(function() {
		
		$(this).css({"background-color": "white"});
		$(this).attr("placeholder", "Your Password");
		
	});

	
});




