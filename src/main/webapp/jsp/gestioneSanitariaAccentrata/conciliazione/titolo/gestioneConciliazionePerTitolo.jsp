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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formConciliazioni">
					<s:hidden name="faseBilancioChiuso" id="hidden_faseBilancioChiuso" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Classi di conciliazione</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal" id="fieldsetRicercaConciliazioni">
								<div class="control-group margin-medium">
									<label class="control-label" for="classeDiConciliazione">Classe di conciliazione *</label>
									<div class="controls">
										<s:select list="listaClasseDiConciliazione" name="classeDiConciliazione" id="classeDiConciliazione" cssClass="span6" headerKey="" headerValue=""
											listValue="descrizione" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Tipo *</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="S" name="entrataSpesaRicerca" <s:if test='%{"S".equals(entrataSpesaRicerca)}'>checked</s:if>>Spese
											</label>
										</span>
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="E" name="entrataSpesaRicerca" <s:if test='%{"E".equals(entrataSpesaRicerca)}'>checked</s:if>>Entrate
											</label>
										</span>
									</div>
								</div>
								<div data-tipo="S" class="hide">
									<div class="control-group">
										<label class="control-label" for="titoloSpesaRicerca">Titolo *</label>
										<div class="controls">
											<s:select list="listaTitoloSpesa" name="titoloSpesaRicerca.uid" id="titoloSpesaRicerca" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="macroaggregatoRicerca">Macroaggregato</label>
										<div class="controls">
											<s:select list="listaMacroaggregato" name="macroaggregatoRicerca.uid" id="macroaggregatoRicerca" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" />
											<span class="clear">
												<button type="button" data-button-cerca class="btn btn-primary pull-right">cerca</button>
											</span>
										</div>
									</div>
								</div>
								<div data-tipo="E" class="hide">
									<div class="control-group">
										<label class="control-label" for="titoloEntrataRicerca">Titolo *</label>
										<div class="controls">
											<s:select list="listaTitoloEntrata" name="titoloEntrataRicerca.uid" id="titoloEntrataRicerca" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="tipologiaTitoloRicerca">Tipologia *</label>
										<div class="controls">
											<s:select list="listaTipologiaTitolo" name="tipologiaTitoloRicerca.uid" id="tipologiaTitoloRicerca" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="categoriaTipologiaTitoloRicerca">Categoria</label>
										<div class="controls">
											<s:select list="listaCategoriaTipologiaTitolo" name="categoriaTipologiaTitoloRicerca.uid" id="categoriaTipologiaTitoloRicerca" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" />
											<span class="clear">
												<button type="button" data-button-cerca class="btn btn-primary pull-right">cerca</button>
											</span>
										</div>
									</div>
								</div>
							</fieldset>
							<div id="divRisultatiRicerca" class="hide">
								<h4 class="step-pane">Elenco conti: <span id="filtroRicerca"></span></h4>
								<table class="table table-hover tab_left" id="tabellaRisultatiRicerca">
									<thead>
										<tr>
											<th id="classificazioneRisultato"></th>
											<th>Classe</th>
											<th>Conto</th>
											<th>Descrizione</th>
											<th class="span1"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<s:if test="%{!faseBilancioChiuso}">
								<p>
									<a id="buttonNuovaConciliazione" class="btn btn-secondary" href="#">Inserisci conto nella classe</a>
								</p>
								<div id="divConciliazione_inserimento" class="hide">
									<s:include value="/jsp/gestioneSanitariaAccentrata/conciliazione/titolo/collapseConciliazioneTitolo.jsp">
										<s:param name="prefix">inserimento</s:param>
										<s:param name="title">Inserisci conto nella classe</s:param>
									</s:include>
								</div>
								<div id="divConciliazione_aggiornamento" class="hide">
									<s:include value="/jsp/gestioneSanitariaAccentrata/conciliazione/titolo/collapseConciliazioneTitolo.jsp">
										<s:param name="prefix">aggiornamento</s:param>
										<s:param name="aggiornamento">true</s:param>
										<s:param name="title">Aggiorna conto della classe</s:param>
									</s:include>
								</div>
							</s:if>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/pianoDeiConti/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/conciliazione/conciliazionePerTitolo.js"></script>
	
</body>
</html>