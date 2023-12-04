<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />

				<fieldset id="fieldsetAssociaQuote">
					<form id="associaQuote" method="post" action="#"> 
						
						<h3>Associa documenti</h3>
						<%-- <h4><s:property value="riepilogoRicerca"/></h4> --%>
						<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />

						<h4 style="float:left;"><span id="id_num_result" class="num_result"></span></h4>
						<h4 style="float:right; margin-right:2%;">Importo predisposizione d'incasso: <span id="importoPreDocumento" class="num_result tab_Right"><s:property value="importoPreDocumento"/></span></h4>
					
							<table class="table table-hover tab_left dataTable" id="risultatiRicercaCollegaDocumento" summary="...." >
							<thead>

								<tr>
									<th role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">
										<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" style="display:none;" id="checkboxSelezionaTutti" disabled>
									</th>
									<th scope="col">Documento</th>
									<th scope="col">Data</th>
									<th scope="col">Stato</th>
									<th scope="col">Soggetto</th>
									<th scope="col">Quota</th>
									<th scope="col">Movimento</th>
									<th scope="col">IVA</th>
									<th scope="col" class="span2">Annotazioni</th>
									<th scope="col" class="tab_Right">Importo quota</th>
									<th scope="col" class="tab_Right">&nbsp; </th>
								</tr>
							</thead>
							<tbody>	
							</tbody>
							<tfoot>
								<tr>
									<th colspan="9">Totale</th>
									<th id="importoTotale" class="tab_Right"><s:property value="importoTotale"/></th>
									<th>&nbsp;</th>
								</tr>
							</tfoot>
						</table>
						
						<s:hidden id="HIDDEN_proseguireConElaborazione" name="proseguireConElaborazione" value="%{proseguireConElaborazione}"/>
						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<a href="#" class="btn btn-primary pull-right" id="associaQuoteAPreDocumento">associa</a>
						</p>  	 
					</form>
				</fieldset>
			</div>
		</div>
	</div>
	

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/collegaDocumentoEntrata.js"></script>