<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:hidden id="HIDDEN_baseUrl" name="baseUrl" />
				<s:hidden id="HIDDEN_baseUrlSubdocumento" name="baseUrlSubdocumento" />
				<s:form action="%{baseUrl}_%{actionMethodName}" cssClass="form-horizontal" novalidate="novalidate">
					<s:hidden name="primaNota.uid" />
					<s:hidden name="primaNota.numero" />
					<s:hidden name="primaNota.statoOperativoPrimaNota" />
					
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="intestazionePagina" /></h3>
					<h4><s:property value="intestazioneRichiesta" escape="false"/></h4>
					
					<div class="accordion" id="accordionMovimento">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionMovimento" href="#collapseMovimento">
									<s:property value="intestazioneMovimentoFinanziario" escapeHtml="true" /><span class="icon">&nbsp;</span>
								</a>
							</div>
							<div id="collapseMovimento" class="accordion-body collapse">
								<div class="accordion-inner">
									<fieldset class="form-horizontal">
										<s:include value="/jsp/contabilitaGenerale/registrazione/consultazione/consultaDati%{consultazioneSubpath}.jsp" />
									</fieldset>
								</div>
							</div>
						</div>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Dati</h4>
								<div class="control-group">
									<label class="control-label" for="dataRegistrazionePrimaNota">Data registrazione *</label>
									<div class="controls">
										<s:textfield id="dataRegistrazionePrimaNota" name="primaNota.dataRegistrazione" cssClass="span2 datepicker" maxlength="10" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizionePrimaNota">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNota" name="primaNota.descrizione" cssClass="span9" required="true" />
									</div>
								</div>
								<h4 class="step-pane">Elenco</h4>
								<table class="table table-hover tab_left" id="tabellaQuote">
									<thead>
										<tr>
											<th class="span1">&nbsp;</th>
											<th>Numero quota</th>
											<th>Causale</th>
											<th>Stato richiesta</th>
											<th>Data registrazione</th>
											<th>Conto finanziario</th>
											<th>Conto finanziario aggiornato</th>
											<th class="tab_Right"></th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="listaElementoQuota" var="elqu">
											<tr>
												<td class="tabRowLight">
													<a data-toggle="modal" data-target="#modaleDettaglioDocumento">
														<p class="pagination-centered">
															<i class="icon-search">&nbsp;</i>
														</p>
													</a>
												</td>
												<td><s:property value="#elqu.numeroQuotaString"/></td>
												<td><s:property value="#elqu.causaleString" escape="false"/></td>
												<td><s:property value="#elqu.statoRichiestaString"/></td>
												<td><s:property value="#elqu.dataRegistrazioneString"/></td>
												<td><s:property value="#elqu.contoFinanziarioInizialeString"/></td>
												<td><s:property value="#elqu.contoFinanziarioString"/></td>
												<td>
													<s:if test="%{#elqu.registrazioneMovFin.uid != 0}">
														<s:a href="%{baseUrlSubdocumento}_enterDettaglio.do?subdocumento.uid=%{#elqu.subdocumento.uid}&movimentoEP.uid=%{#elqu.movimentoEP.uid}&uidRegistrazione=%{#elqu.registrazioneMovFin.uid}"
															cssClass="btn btn-secondary" data-dettaglio="">Dettaglio</s:a>
													</s:if>
												</td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:if test="aggiornamento and not validazione or validazioneAbilitata">
							<span class="pull-right">
								<s:submit cssClass="btn btn-primary" value="%{testoPulsanteSubmit}" />
							</span>
						</s:if>
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleDettaglioDocumento.jsp" />
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/primaNotaIntegrata/gestisci.documento.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/registrazione/consultaRegistrazioneMovFin${consultazioneSubpath}.js"></script>
</body>
</html>