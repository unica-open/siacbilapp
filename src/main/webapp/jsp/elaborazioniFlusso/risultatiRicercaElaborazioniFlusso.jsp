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

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>

					<form method="post" action="#">
						<h3>Risultati di ricerca  PagoPA</h3>
						<h4><s:property value="riepilogoRicerca"/></h4>

						<h4><span id="id_num_result" class="num_result"></span></h4>

							<table class="table table-hover tab_left dataTable" id="risultatiRicercaDocumento" summary="....">
							<thead>
								<tr>
									<th scope="col" style="text-align: center;">Numero Provvisorio</th>
									<th scope="col" style="text-align: center;">Data Emissione</th>
									<th scope="col" style="text-align: center;">Versante</th>
									<th scope="col" style="text-align: center;">Flusso</th>
									<th scope="col" style="text-align: center;">Importo Provvisorio</th>
									<th scope="col" style="text-align: center;">Data Elaborazione</th>
									<th scope="col" style="text-align: center;">Esito Elaborazione Flusso</th> <%-- SIAC-7975 - CONTABILIA-333 CM 02/03/2021 cambiato nome etichetta --%>
									<th scope="col" >&nbsp; </th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
							</tfoot>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />

						
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
	<script type="text/javascript" src="/siacbilapp/js/local/elaborazioniFlusso/risultatiRicerca.js"></script>
</body>
</html>