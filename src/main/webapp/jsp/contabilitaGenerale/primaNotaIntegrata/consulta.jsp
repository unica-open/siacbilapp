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
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formConsultaPrimaNotaLibera">
					<s:hidden name="primaNota.uid" id="uidPrimaNotaIntegrata" />
					<s:hidden name="primaNota.numero" id="numeroPrimaNotaIntegrata" />
					<s:hidden name="primaNota.descrizione" id="descrizionePrimaNotaIntegrata" />
					<s:hidden name="tipoCollegamentoDatiFinanziari" id="tipoCollegamentoDatiFinanziari" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Prima Nota <s:property value="numeroPrimaNota"/> </h3>
					<div class="step-pane active" id="step1">
					<fieldset class="form-horizontal">                                    
						<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/fielsetConsultazionePrimaNota.jsp" />
						<div class="clear"></div>
						<br />
						<h4>Elenco scritture</h4>
							<table class="table table-hover tab_left" id="tabellaScritture">
								<thead>
									<tr>
										<th>Conto</th>
										<th>Descrizione</th>
										<th class="tab_Right">Dare</th>
										<th class="tab_Right">Avere</th>
									</tr>
								</thead>
								<tbody>
<%-- 									<s:iterator value="primaNota.listaMovimentiEP" var="movEP"> --%>
										<s:iterator value="listaMovimentoDettaglio" var="movDett">
											<tr>
												<td><s:property value="#movDett.conto.codice" /></td>
												<td><s:property value="#movDett.conto.descrizione" /></td>
												<td class="tab_Right">
													<s:if test='%{"DARE".equals(#movDett.segno.name())}'>
														<s:property value="#movDett.importo" />
													</s:if>
												</td>
												<td class="tab_Right">
													<s:if test='%{"AVERE".equals(#movDett.segno.name())}'>
														<s:property value="#movDett.importo" />
													</s:if>
												</td>
											</tr>
										</s:iterator>
<%-- 									</s:iterator> --%>
								</tbody>
								<tfoot>
									<tr>
										<th colspan="2">Totale</th>
										<th class="tab_Right" id="totaleDare"><s:property value="totaleDare"/></th>
										<th class="tab_Right" id="totaleAvere"><s:property value="totaleAvere"/></th>
									</tr>
								</tfoot>
							</table>
						</fieldset>
					</div>
					<div class="Border_line"></div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:if test='%{validazioneAbilitata}'>
							<button type="button" id="validazionePrimaNotaIntegrata" class="btn btn-primary pull-right">valida</button>
						</s:if>
					</p>
					<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleValidazione.jsp" />
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/primaNotaIntegrata/consulta.js"></script>

</body>
</html>