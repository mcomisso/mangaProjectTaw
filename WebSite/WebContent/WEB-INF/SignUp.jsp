<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SignUp</title>
<link rel="stylesheet" href="css/foundation.css" /> <!-- Foundation Framework -->
	<script src="js/vendor/modernizr.js"></script> <!-- Foundation Framework -->
	<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"> <!-- Icone -->
</head>
<body>
<nav class="top-bar" data-topbar>
	<ul class="title-area">
		<li class="name">
			<h1><a href="/WebSite">Homepage</a></h1>
		</li>
		<li class="toggle-topbar menu-icon">
			<a href="#"><span>Menu</span></a>
		</li>
	</ul>
	<!-- Right side of menu -->
	<section class="top-bar-section">
		<ul class="right">
			<li class="active"><a href="myManga">My Manga</a></li>
			<li><a href="/WebSite/LogIn">Login</a></li>
		</ul>
	</section>
</nav>
<div class="row">
	<div class="columns large-12">
		<h1>SignUp</h1>
		${requestScope.message}
	</div>
	<div class="column large-12">
		<form action="Signupping" method="post" data-abide>
		<fieldset>
		<legend>Inserisci i tuoi dati</legend>
			<label> Nome 
				<input type="text" name="firstname">
			</label>
			<label> Cognome
				<input type="text" name="lastname">
			</label>
			<div class="email-field">
			<label> Email <small>Required</small>
				<input type="email" name="email" required>
			</label>
			<small class="error">Inserire un indirizzo email valido.</small>
			</div>
			<label> Password <small>Required</small>
				<input type="password" name="pwd">
			</label>
			<input class="button" type="submit">
			</fieldset>
		</form>
	</div>
</div>
<!-- Foundation include -->
    <script src="js/vendor/jquery.js"></script>
    <script src="js/foundation.min.js"></script>
    <script>
      $(document).foundation();
    </script>
</body>
</html>