<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<p class="nascosto">
		<a id="A-sommario" title="A-sommario"></a>
	</p>
	<ul id="sommario" class="nascosto">
		<li><a href="#A-contenuti">Salta ai contenuti</a></li>
	</ul>
	<hr>
	<div class="container-fluid-banner">
		<r:include url="/ris/servizi/siac/include/portalheader.html" resourceProvider="rp" />
		<s:include value="/jsp/include/infoUtenteLogin.jsp" />
		<r:include url="/ris/servizi/siac/include/applicationHeader.html" resourceProvider="rp" />
		<a id="A-contenuti" title="A-contenuti"></a>
		<div class="row-fluid">
			<div class="span12">
				<ul class="breadcrumb">
					<s:url action="redirectToCruscotto" var="homeURL" />
					<li><a href="${homeURL}">Home</a></li>
					<li><span class="divider">&gt;</span></li>
					<li class="active">Service Cache</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="step-content">
					<div class="step-pane active">
						<div class="alert alert-error hide" id="ERRORI">
							<button type="button" class="close" data-hide="alert">&times;</button>
							<strong>Attenzione!!</strong>
							<br/>
							<ul></ul>
						</div>
						<div class="alert alert-success hide" id="INFORMAZIONI">
							<button type="button" class="close" data-hide="alert">&times;</button>
							<strong>Informazioni</strong><br>
							<ul></ul>
						</div>
						<p class="pull-right margin10-0">
							<span class="btn-toolbar">
								<span class="btn-group">
									<button type="button" class="btn pull-right" onclick="cacheManager.refresh(true)"><abbr title="Accurate">Acc.</abbr> refresh <i class="icon icon-refresh"></i></button>
									<button type="button" class="btn pull-right" onclick="cacheManager.refresh(false)">Refresh <i class="icon icon-refresh"></i></button>
								</span>
								<button type="button" class="btn pull-right" onclick="cacheManager.clear()">Clear cache <i class="icon icon-trash"></i></button>
							</span>
						</p>
						<h2>Service Cache</h2>
						<h4>General informations</h4>
						<pre id="statistics"></pre>
						<h4>
							Keys in cache
							<a class="tooltip-test" title="How-to decode" onclick="cacheManager.toggleHowToDecode()" data-tooltip>
								<i class="icon-info-sign">&nbsp;
									<span class="nascosto">How-to decode</span>
								</i>
							</a>
							<span id="howToDecode" class="fade">
								CachedServiceExecutor_&lt;service_name&gt;_&lt;ente_id&gt;_&lt;other_keys&gt;
							</span>
						</h4>
						<table id="keys" class="table table-striped table-bordered table-hover dataTable">
							<thead class="hide">
								<tr>
									<td class="span11"></td>
									<td class="span1"></td>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<script type="text/javascript" src="${jspathexternal}${jquery.version}.js"></script>
	<script type="text/javascript" src="${jspathexternal}bootstrap-alert.js"></script>
	<script type="text/javascript" src="${jspathexternal}bootstrap-tooltip.js"></script>
	<script type="text/javascript" src="${jspathexternal}dataTable/jquery.dataTables${datatables.version}.js"></script> 
	<script type="text/javascript" src="${jspathexternal}dataTable/bootstrap.dataTables.min.js"></script>
	<script type="text/javascript" src="${jspathexternal}dataTable/bootstrap.dataTables.firstlast.js"></script>
	<script type="text/javascript" src="${jspath}jquery.bs.overlay.js"></script>
	
	<script type="text/javascript" src="${jspath}polyfill.js"></script>
	<script type="text/javascript" src="${jspath}cache/btoa.min.js"></script>
	<script type="text/javascript" src="${jspath}cache/utils.js"></script>
	<script type="text/javascript" src="${jspath}cache/cache.js"></script>
</body>
</html>