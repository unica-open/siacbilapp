<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<!-- Corpo pagina-->

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<form method="post" action="#">
						<s:include value="/jsp/include/messaggi.jsp" />
						
						<h3><span id="id_num_result" class="num_result"></span> Risultati trovati</h3>
						<s:hidden id="HIDDEN_startPosition" name="savedDisplayStart" />
						<table class="table table-striped table-bordered table-hover dataTable" id="risultatiricerca" summary="...." >
							<thead>
								<tr>
									<th scope="col">Capitolo</th>
									<th scope="col">Classificazione</th>
									<th scope="col">Competenza</th>
									<th scope="col">Residuo</th>
									<th scope="col">Cassa</th>
									<th scope="col">Azioni</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<th scope="col">Capitolo</th>
									<th scope="col">Classificazione</th>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamento"/>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamentoResiduo"/>
									</th>
									<th scope="col" class="text-right">
										<s:property value="totaleImporti.stanziamentoCassa"/>
									</th>
									<th scope="col">Azioni</th>
								</tr>
							</tfoot>
						</table>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}capitolo/risultatiRicerca.js"></script>
	<script type="text/javascript" src="${jspath}capitoloUscitaPrevisione/risultatiRicercaMassiva.js"></script>
</body>
</html>