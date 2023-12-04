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
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:form action="effettuaRicercaProgetto" novalidate="novalidate">
						<h3>Ricerca Progetto</h3>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<div class="step-content">
							<div class="step-pane active" id="step1">
								<p><s:submit cssClass="btn btn-primary pull-right" value="cerca" /></p>
								<h4 class="step-pane">Dati generali</h4>
								<fieldset class="form-horizontal">
									
									<div class="control-group">
										<label class="control-label" for="codiceProgetto">Codice Progetto </label>
										<div class="controls">
											<s:textfield id="codiceProgetto" cssClass="lbTextSmall span3" name="progetto.codice" maxlength="20" placeholder="Codice Progetto" required="required" />
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="codiceProgetto">Anno Bilancio</label>
										<div class="controls">
											<s:textfield id="anno" cssClass="lbTextSmall span1 soloNumeri" name="progetto.bilancio.anno" maxlength="4" placeholder="anno"  value="%{annoEsercizio}" />
										</div>
									</div>
									
									<div class="control-group">
										<label for="ambito" class="control-label">Ambito</label>
										<div class="controls input-append">
											<s:select list="listaTipiAmbito" cssClass="span8" name="progetto.tipoAmbito.uid" id="ambito" headerKey="" headerValue=""
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="rilFPV">Rilevante a FPV </label>
										<div class="controls">
											<select name="flagFondoPluriennaleVincolato" id="rilFPV">
												<option value="">Non si applica</option>
												<option value="S">S&iacute;</option>
												<option value="N">No</option>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label for="stato" class="control-label">Stato</label>
										<div class="controls input-append">
											<s:select list="listaStatoProgetto" cssClass="span8" name="progetto.statoOperativoProgetto" id="stato" headerKey="" headerValue="" />
										</div>
									</div>
		
									<div class="control-group">
										<label for="descrizioneProgetto" class="control-label">Descrizione</label>
										<div class="controls">
											<s:textarea id="descrizioneProgetto" rows="5" cols="15" cssClass="span8" name="progetto.descrizione" maxlength="500"></s:textarea>
										</div>
									</div>
									<div class="control-group">
										<label for="dataIndizioneGara" class="control-label">Data validazione progetto a base di gara</label>
										<div class="controls">
											<s:textfield id="dataIndizioneGara" name="progetto.dataIndizioneGara" cssClass="span2 datepicker" />
										</div>
									</div>
									<div class="control-group">
										<label for="dataAggiudicazioneGara" class="control-label">Anno Programmazione</label>
										<div class="controls">
											<s:textfield id="dataAggiudicazioneGara" name="progetto.dataAggiudicazioneGara" cssClass="span2 datepicker" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="rilFPV">Investimento in corso di definizione </label>
										<div class="controls">
											<select name="flagInvestimentoInCorsoDiDefinizione" id="investimentoInCorsoDiDefinizione">
												<option value="">Non si applica</option>
												<option value="S">S&iacute;</option>
												<option value="N">No</option>
											</select>
										</div>
									</div>
								</fieldset>
								<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
							</div>								
						</div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:reset cssClass="btn btn-link" value="annulla" />
							<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/progetto/ricercaProgetto.js"></script>
</body>
</html>