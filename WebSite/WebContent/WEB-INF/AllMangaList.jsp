<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>All Manga List</title>
	<link rel="stylesheet" href="css/foundation.css" /> <!-- Foundation Framework -->
	<script src="js/vendor/modernizr.js"></script> <!-- Foundation Framework -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
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
				<li class=""><a href="LogOut">Logout</a></li>
			</ul>
		</section>
	</nav>
	
	<div class="row">
		<h1>Lista manga completa</h1>
	<div class="columns large-12">

	<script>
	$(document).ready(function(){
		$('html').bind('keypress', function(e) { if(e.keyCode == 13) { return false; } });
		var timer;
		$("#searchbox").keyup(function () {
		    clearTimeout(timer);
		    timer = setTimeout(function(event){
		    	var searchtext = $("#searchbox").val();
		    	if (!searchtext)
		    	{
		    		serachtext = "";
		    	}
			    $.post("/WebSite/search", {value:searchtext}, function(data){
			    	//console.log(data);
			    	var isLogged = '<c:if test="${not empty loggedUser}">true</c:if>';
			    	$('#tableBody').html('');
			    	for (var i in data)
			    	{
			    		if (data.hasOwnProperty(i))
			    		{
					    	/* Controlla se sl'utente e' loggato-> Y: stampa il form */
					    	var gimmeSomeLove = "";
					    	var user = "";
					    	var date = new Date(data[i].lastdate);
			    			var form = "";
					    	
					    	if (isLogged == 'true')
				    		{
					    		var manga = "" + data[i].IDManga + '"';
					    		gimmeSomeLove = "<td>";
					    		//gimmeSomeLove = '<td><a href="javascript:{}" onclick="document.getElementById("' + manga + ').submit();">Add<i class="fa fa-heart"></i></a>';
				    			user = "<c:out value='${loggedUser.id}'></c:out>";
				    			form = '<div><form id="' + data[i].IDManga + '" action="addToFavorites" method="post"><input type="hidden" name="mangaID" value="' + data[i].IDManga + '"/><input type="hidden" name="userID" value="' + user + '"/><div><input class="button" value="add" type="submit" style="width: 15px !important; height:15px !important;" name="muchLove"/></div></form></div></td>';
				    		}
					    	else
					    	{
					    		gimmeSomeLove = '';
					    		form = '</td>';
					    	}
// Set della lingua
			    			if(data[i].lang == "1")
			    			{
				    			$('#tableBody')
				    			.append('<tr><td><a href="http://www.mangaeden.com/it-manga/' + data[i].alias + '"><i class="fa fa-flag"></i>ITA ' + data[i].title + '</a></td><td>' + date.toString() + '</td><td>' + data[i].hits + '</td>' + gimmeSomeLove + form + '</tr>');
			    			}
			    			else
			    			{
			    				$('#tableBody')
				    			.append('<tr><td><a href="http://www.mangaeden.com/en-manga/' + data[i].alias + '"><i class="fa fa-flag-o"></i>ENG ' + data[i].title + '</a></td><td>' + date.toString() + '</td><td>' + data[i].hits + '</td>' + gimmeSomeLove + form + '</tr>');
				    		}
			    		}
			    	}
			    });
		    }, 400);
		});
	});
	</script>
	
	
	
	<form>
	  <div class="row collapse">
	    <div class="small-3 large-2 columns">
	      <span class="prefix"><i class="fa fa-search"></i>Ricerca</span>
	    </div>
	    <div class="small-9 large-10 columns">
	      <input id="searchbox" type="text" placeholder="Cosa stai cercando?">
	    </div>
	  </div>
	</form>
	<div class="row">
	<div class="columns large-12">
	</div>
	<table>
		<thead>
		<tr>
			<th>Title</th>
			<th>Last Update</th>
			<th>Hits</th>
			<c:if test="${not empty loggedUser}"><th><i class="fa fa-heart"></i></th></c:if>
		</tr>
		</thead>
		<tbody id="tableBody">
			<c:forEach items="${allList}" var="manga">
				<tr>
					<td><a href="${manga.getLinkManga()}">${manga.getTitle()}</a></td>
					<td>${manga.getLastDateFormatted()}</td>
					<td>${manga.getHits()}</td>
					<c:if test="${not empty loggedUser}"><th><i class="fa fa-heart"></i></th></c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
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