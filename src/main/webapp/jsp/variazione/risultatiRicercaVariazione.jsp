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
						<h3>Risultati di ricerca Variazioni</h3>
						<h3><span id="id_num_result" class="num_result"></span> Risultati trovati</h3>

						<s:hidden name="tipoVariazioneCodifica" id="tipoVariazione" />
						<s:hidden name="savedDisplayStart" id="startPosition" />
						<table class="table table-striped table-bordered table-hover dataTable" id="risultatiRicercaVariazioni" summary="...." >
							<thead>
								<tr>
									<th scope="col">Numero</th>
<!-- 									<th scope="col">Applicazione</th> -->
									<th scope="col">Descrizione</th>
									<s:if test="!tipoVariazioneCodifica">
										<th scope="col">Dir. Proponente</th>
									</s:if>
									<th scope="col">Tipo</th>
									<s:if test="tipoVariazioneCodifica">
										<th scope="col">Provvedimento</th>
										<th scope="col">Stato</th>
									</s:if><s:else>
										<th scope="col">Provvedimento PEG</th>
										<th scope="col">Provvedimento Variazione di Bilancio</th>
										<th scope="col">Data apertura proposta</th>
										<th scope="col">Data chiusura proposta</th>
										<th scope="col">Stato</th>
										<th scope="col">Operazione effettuata </th>
										<th scope="col">Stato operazione</th>
										<th scope="col">Data operazione </th>
									</s:else>
									
									
									<th scope="col">Azioni</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						
						
						
	<s:hidden id="idAzioneReportVariazioni" name="idAzioneReportVariazioni"/>
	
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/modaleStampaVariazioni.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/stampaVariazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/risultatiRicerca.js"></script>

</body>
</html>