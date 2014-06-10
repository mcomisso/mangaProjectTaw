<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>HomePage TAWProj</title>
	<link rel="stylesheet" href="css/foundation.css" /> <!-- Foundation Framework -->
	<script src="js/vendor/modernizr.js"></script> <!-- Foundation Framework -->
	<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"> <!-- Icone -->
	<style>
		img{
			height:200px;
		}
	</style>
	
</head>
<!-- BODY START HERE -->
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
			<li class=""><a href="/WebSite/listManga"><i class="fa fa-search"></i>Cerca manga</a></li>
			<c:choose>
				<c:when test="${empty loggedUser}">
					<li class="active"><a href="myManga">LogIn</a></li>
				</c:when>
				<c:otherwise>
					<li class="active"><a href="myManga">My Manga</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</section>
</nav>
<div class="row">
	<div class="columns large-12">
	<div class="panel">
	<img alt="LOGO" src="img/Taw-01.png"/>
</div>
		<h3>Gli ultimi 20 manga aggiornati:</h3>
	</div>
</div>
<div class="row">
<div class="large-12 columns">

<!-- WARNINGS DI ECLIPSE - IL DIV DI APERTURA È ALL'INTERNO DI UN CHOOSE/WHEN/OTHERWISE -->
	<ul class="small-block-grid-2 medium-block-grid-3 large-block-grid-4">
	    <c:forEach items="${mangas20}" var="manga">
			<li> 
   
   				<div class="hide">
					<form id="${manga.getIDManga()}" action="addToFavorites" method="post">
						<input type="hidden" name="mangaID" value="${manga.getIDManga()}"/>
						<input type="hidden" name="userID" value="${loggedUser.id}"/>
						<div class="hide">
							<input type="submit" style="width: 1px; height:1px;" name="muchLove"/>
						</div> 
					</form>
				</div>
				<ul class="pricing-table">
					<li style="height: 50px;" class="title">
					<c:if test="${allMangaOfUser.contains(manga)}"><i class="fa fa-heart"></i></c:if>
					${manga.getTitle()}
					<c:if test="${allMangaOfUser.contains(manga)}"><i class="fa fa-heart"></i></c:if>
					</li>
					<li class="price">
						<a class="th [radius]" href="${manga.getLinkManga()}">
							<img class="" src="${manga.getLinkImg()}" />
						</a>
					</li>
					<li class="description">${manga.getLastDateFormatted()}</li>
					<li class="description"><i class="fa fa-flag-o"></i>
					<c:choose>
						<c:when test="${manga.getLang() == '1'}">ITA</c:when>
						<c:otherwise>ENG</c:otherwise> 
					</c:choose></li>
					<c:if test="${not empty loggedUser}">
					<li class="cta-button">
						<c:choose>
							<c:when test="${allMangaOfUser.contains(manga)}">
							<!-- Se il manga è contenuto nell'elenco -> visualizza la bomba -->
								<a href="javascript:{}" onclick="document.getElementById('${manga.getIDManga()}').submit();" class="button alert [tiny small radius round]">
									 <i class="fa fa-thumbs-o-down"></i>
								</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:{}" onclick="document.getElementById('${manga.getIDManga()}').submit();" class="button [tiny small radius round]">
									<i class="fa fa-thumbs-o-up"></i>
								</a>
							</c:otherwise>
						</c:choose>
					</li>
					</c:if>
				</ul>
		        </li>
	    </c:forEach>
    </ul>
</div>
</div>
<!-- Foundation include -->
    <script src="js/vendor/jquery.js"></script>
    <script src="js/foundation.min.js"></script>
    <script>
      $(document).foundation();
    </script>
</body>
<!-- BODY END HERE -->
</html>