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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formRichiestaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneRisultatiRicerca" /></h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<s:hidden name="hasNumeroRichiesta" id="HIDDEN_hasNumeroRichiesta" />
								<s:hidden name="hasData" id="HIDDEN_hasData" />
								<s:hidden name="hasStato" id="HIDDEN_hasStato" />
								<s:hidden name="hasNumeroSospeso" id="HIDDEN_hasNumeroSospeso" />
								<s:hidden name="hasNumeroMovimento" id="HIDDEN_hasNumeroMovimento" />
								<s:hidden name="hasRichiedente" id="HIDDEN_hasRichiedente" />
								<s:hidden name="hasImporto" id="HIDDEN_hasImporto" />
								<s:hidden name="hasNumeroMovimentoRendiconto" id="HIDDEN_hasNumeroMovimentoRendiconto" />
								<s:hidden name="savedDisplayStart" id="HIDDEN_savedDisplayStart" />
								
								<h4 class="step-pane">
									<span class="num_result"></span>
								</h4>
								<table class="table table-hover tab_left" id="tabellaRisultatiRicerca">
									<thead>
										<tr>
											<s:if test="hasNumeroRichiesta"><th>N. richiesta</th></s:if>
											<s:if test="hasData"><th>Data</th></s:if>
											<s:if test="hasStato"><th>Stato</th></s:if>
											<s:if test="hasNumeroSospeso"><th>N. sospeso</th></s:if>
											<s:if test="hasNumeroMovimento"><th>N. movimento</th></s:if>
											<s:if test="hasNumeroMovimentoRendiconto"><th>N.Movim.Rendic.</th></s:if>
											<s:if test="hasRichiedente"><th>Richiedente</th></s:if>
											<s:if test="hasImporto"><th class="tab_Right"><s:property value="intestazioneImporto"/></th></s:if>
											<th class="tab_Right span1">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<s:if test="hasImporto">
										<tfoot>
											<tr>
												<th colspan="<s:property value="colspanTotaleImporto" />"><s:property value="intestazioneTotaleImporto"/></th>
												<th class="tab_Right" id="totaleImporto"></th>
												<th>&nbsp;</th>
											</tr>
										</tfoot>
									</s:if>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/cassaEconomale/tabelle/modaleAnnullamento.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cassaEconomale/richieste/risultatiRicerca.js"></script>
	<script type="text/javascript" src="${jspath}cassaEconomale/richieste/<s:property value="pathTipoRichiestaEconomale" />/risultatiRicerca.js"></script>
	
</body>
</html>