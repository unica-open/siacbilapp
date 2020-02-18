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
				<s:form action="%{baseUrlSubdocumento}_%{actionMethodName}" cssClass="form-horizontal" novalidate="novalidate">
					<s:hidden name="primaNota.uid" />
					<s:hidden name="primaNota.numero" />
					<s:hidden name="primaNota.statoOperativoPrimaNota" />
					<s:hidden name="primaNota.dataRegistrazione" />
					<s:hidden name="primaNota.descrizione" />
					<s:hidden name="movimentoEP.uid" />
					<s:hidden name="movimentoEP.numero" />
					<s:hidden name="subdocumento.uid" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="intestazionePagina" /></h3>
					<h4><s:property value="intestazioneRichiesta" escape="false"/></h4>
					<h4><s:property value="intestazioneMovimentoFinanziario"/></h4>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Dettaglio</h4>
								<div class="control-group">
									<label class="control-label" for="causaleEPmovimentoEP">Causale *</label>
									<div class="controls">
										<s:select list="listaCausaleEP" name="movimentoEP.causaleEP.uid" id="causaleEPmovimentoEP" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span6" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizioneMovimentoEP">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneMovimentoEP" name="movimentoEP.descrizione" cssClass="span9" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="noteMovimentoEP">Note</label>
									<div class="controls">
										<s:textfield id="noteMovimentoEP" name="movimentoEP.note" cssClass="span9" required="true" />
									</div>
								</div>
								<h4 class="nostep-pane">Da registrare (D-A): <span id="daRegistrare"><s:property value="daRegistrare"/></span></h4>
								<h4 class="step-pane">Elenco scritture</h4>
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
											<th class="tab_Right"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="2">Totale</th>
											<th class="tab_Right" id="totaleDare"><s:property value="totaleDare" /></th>
											<th class="tab_Right" id="totaleAvere"><s:property value="totaleAvere" /></th>
											<th class="tab_Right">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p>
									<%-- Sempre disabilitato per le integrate --%>
									<button type="button" class="btn btn-secondary disabled">inserisci dati in elenco</button>
								</p>
							</fieldset>
						</div>
					</div>
					<p class="margin-large">
						<s:a cssClass="btn" action="%{urlBackToStep1}" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<span class="pull-right">
							<s:if test="aggiornamento">
								<s:submit cssClass="btn btn-primary" value="aggiorna" />
							</s:if><s:else>
								<s:submit cssClass="btn btn-primary" value="salva" />
							</s:else>
						</span>
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleEliminazioneConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAggiornaConto.jsp" />
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/primaNotaIntegrata/gestisci.subdocumento.js"></script>
</body>
</html>