<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<c:choose>
	<c:when test="${empty loggedUser}">
		<title>Login Sigup</title>
	</c:when>
	<c:otherwise>
		<title>MyManga</title>
	</c:otherwise>
</c:choose>

<link rel="stylesheet" href="css/foundation.css" /><!-- Foundation Framework -->
<script src="js/vendor/modernizr.js"></script><!-- Foundation Framework -->
<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"
	rel="stylesheet">
<!-- Icone -->
</head>
<body>
	<nav class="top-bar" data-topbar>
		<ul class="title-area">
			<li class="name">
				<h1>
					<a href="/WebSite">Homepage</a>
				</h1>
			</li>
		</ul>
		<!-- Right side of menu -->
		<section class="top-bar-section">
			<ul class="right">
				<c:choose>
					<c:when test="${empty loggedUser}">
						<li class="active"><a href="myManga">My Manga</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="editData">Modifica Dati Personali</a></li>
						<li><a href="editPwd">Modifica Password</a></li>
						<li><a href="deleteUser">Elimina Account</a></li>
						<li class="active"><a href="LogOut">Logout</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</section>
	</nav>
	<c:choose>
		<c:when test="${empty loggedUser}">
			<div class="row">
				<div class="columns large-12">
					<div class="columns large-3"></div>
					<div class="columns large-6 panel">
						<p class="text-center">Registrati o effettua il login</p>
						<a class="button" href="LogIn">LogIn</a> 
						<a class="button success right"
							href="SignUp">SignUp</a>
					</div>
					<div class="columns large-3"></div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="row">
				<div class="columns large-12">
					<p>
						Ciao
						<c:out value="${requestScope.loggedUser.firstname}" />
					<div class="row">
						<div class="columns large-12">
							<c:choose>
								<c:when test="${empty listOfUserMangaLikes}">
									<h3>Aggiungi alcuni manga nella tua lista di preferiti per vederli in questa pagina</h3>
								</c:when>
								<c:otherwise>
									<h3>Le serie che stai tenendo d'occhio:</h3>
									<ul
										class="small-block-grid-2 medium-block-grid-3 large-block-grid-4"
										data-equalizer>
										<c:forEach items="${listOfUserMangaLikes}" var="mangaLiked">
										<li>
											<div class="hide">
												<form id="${mangaLiked.getIDManga()}" action="addToFavorites" method="post">
													<input type="hidden" name="mangaID" value="${mangaLiked.getIDManga()}"/>
													<input type="hidden" name="userID" value="${loggedUser.id}"/>
													<input type="hidden" name="isMyMangaPage" value="yes"/>
													<div class="hide">
														<input type="submit" style="width: 1px; height:1px;" name="muchLove"/>
													</div> 	
												</form>
											</div>
										<ul class="pricing-table">
											<li class="title">${mangaLiked.getTitle()}</li>
											<li class="price">
												<a class="th [radius]" href="${mangaLiked.getLinkManga()}">
													<img src="${mangaLiked.getLinkImg()}" />
												</a>
											</li>
											<li class="description">${mangaLiked.getLastDateFormatted()}</li>
											<li class="description"><i class="fa fa-flag-o"></i>
												<c:choose>
													<c:when test="${mangaLiked.getLang() == '1'}">ITA</c:when>
													<c:otherwise>ENG</c:otherwise> 
												</c:choose>
											</li>
											<li class="cta-button">
												<a href="javascript:{}" 
													onclick="document.getElementById('${mangaLiked.getIDManga()}').submit();" 
													class="button alert [tiny small radius round]">
														<i class="fa fa-thumbs-o-down"></i>
												</a>
											</li>
										</ul>
											</li>
										</c:forEach>
									</ul>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	<!-- Foundation include -->
	<script src="js/vendor/jquery.js"></script>
	<script src="js/foundation.min.js"></script>
	<script>
	$(document).ready(function(){
      	$(document).foundation();
	});
    </script>
</body>
</html>