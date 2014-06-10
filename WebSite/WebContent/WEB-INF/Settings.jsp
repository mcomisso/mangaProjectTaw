<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Settings</title>
<link rel="stylesheet" href="css/foundation.css" /> <!-- Foundation Framework -->
<script src="js/vendor/modernizr.js"></script> <!-- Foundation Framework -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"/> <!-- Icone -->
</head>
<body>
	<nav class="top-bar" data-topbar>
		<ul class="title-area">
			<li class="name">
				<h1>
					<a href="/WebSite">Homepage</a>
				</h1>
			</li>
			<li class="toggle-topbar menu-icon"><a href="#"><span>Menu</span></a>
			</li>
		</ul>
		<!-- Right side of menu -->
		<section class="top-bar-section">
			<ul class="right">
				<li class="active"><a href="myManga">My Manga</a></li>
				<li class="has-dropdown"><a href="#">Impostazioni</a>
					<ul class="dropdown alert">
						<li><a href="editData">Modifica Dati Personali</a>
						<li><a href="editPwd">Modifica Password</a>
						<li><a href="deleteUser">Elimina Account</a>
						<li class="active"><a href="LogOut">Logout</a></li>
					</ul></li>
			</ul>
		</section>
	</nav>
<div class="row">
	<div class="columns large-12">
		<h1>Impostazioni</h1>
	</div>
</div>
<div class="row">
<div class="columns large-12">
	<c:if test="${not empty requestScope.message}">
		<script type="text/javascript">alert('<c:out value="${requestScope.message}"/>');</script>
	</c:if>
	<c:choose>
		<c:when test="${requestScope.TODO eq 'editData'}">
			<form action="editingData" method="post">
			<fieldset><legend>Modifica i dati personali</legend>
				<label>Nome
				<input type="text" name="firstname" value='<c:out value="${requestScope.loggedUser.firstname}" />'>
				</label>
				<label>Cognome 
					<input type="text" name="lastname" value='<c:out value="${requestScope.loggedUser.lastname}" />'>
				</label>
				<label>Email 
					<input type="text" name="email" value='<c:out value="${requestScope.loggedUser.email}" />'>
				</label>
				<input type="hidden" name="userId" value='<c:out value="${requestScope.loggedUser.id}" />'>
				<input class="button" type="submit">
				</fieldset>
			</form>
		</c:when>
		<c:when test="${requestScope.TODO eq 'editPwd'}">
			<form action="editingPwd" method="post">
				<fieldset>
					<legend>Modifica Password</legend>
						<label>Vecchia Password <small>Required</small>
						<input type="password" name="oldPwd">
						</label>
						<label>Nuova Password <small>Required</small> 
						<input type="password" name="newPwd">
						</label>
						<label>Ripetere Nuova Password <small>Required</small> 
						<input type="password" name="verifyNewPwd">
						</label>
						<input class="button" type="submit">
				</fieldset>
			</form>
		</c:when>
		<c:when test="${requestScope.TODO eq 'deleteUser'}">
			<h2>Confermare eliminazione utente, <b>operazione non reversibile!</b></h2>
			<a href="#" data-reveal-id="confirmModal" class="button">Elimina Utente</a>
			<div id="confirmModal" class="reveal-modal" data-reveal>
			<form action="deletingUser" method="post">
				<fieldset>
				<legend>Conferma eliminazione</legend>
					<h3>Non sarà più possibile accedere al sistema.</h3>
					<input class="button alert" type="submit" value="Si, cancellami">
				</fieldset>
			</form>
			<a class="close-reveal-modal">&#215;</a>
			</div>
		</c:when>
	</c:choose>
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