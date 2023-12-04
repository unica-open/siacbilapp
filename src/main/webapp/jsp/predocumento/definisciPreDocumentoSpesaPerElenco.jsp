<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
				<s:form action="definisciPreDocumentoSpesaPerElenco_completaDefinisci" novalidate="novalidate" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Definisci predocumento di pagamento per elenco</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca</p>
					<div class="step-content">
						<h4 class="step-pane active">Elenco</h4>
						<div class="control-group" id="divRicercaElenco">
							<label class="control-label" for="annoElencoDocumentiAllegato">Anno</label>
							<div class="controls">
								<s:textfield id="annoElencoDocumentiAllegato" name="elencoDocumentiAllegato.anno" maxlength="7"	cssClass="span1 soloNumeri" placeholder="anno" />
								<span class="alRight">
									<label class="radio inline" for="numeroElencoDocumentiAllegato">Numero</label>
								</span>
								<s:textfield id="numeroElencoDocumentiAllegato" name="elencoDocumentiAllegato.numero" placeholder="numero" maxlength="7" cssClass="span2 soloNumeri" />
								<s:hidden id="HIDDEN_uidElencoDocumentiAllegato" name="elencoDocumentiAllegato.uid"></s:hidden>
								<%-- <span class="radio guidata">
									<a class="btn btn-primary" data-toggle="modal" id="pulsanteCompilazioneGuidataElenco">compilazione guidata</a>
								</span> --%>
								<span class="pull-right">
									<button id="pulsanteRicercaElenco" type="button" class="btn btn-primary pull-right"> cerca</button>
								</span>
							</div>
						</div>
						<div id="totaliElencoPredocumenti" class="hide">
							<h4 class="step-pane active">&nbsp;</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="numeroPredisposizioniTotale">Numero Predisposizioni </label>
									<div class="controls">
										<input type="text" value="" name="numeroPreDocumentiTotale" readonly="readonly" class="span1 soloNumeri" id="numeroPredisposizioniTotale">
										<span class="al">
											<label class="radio inline" for="importoPredisposizioniTotale">Totale euro</label>
										</span>
										<input type="text" value="" class="span2 soloNumeri" readonly="readonly" id="importoPredisposizioniTotale">									
									</div>							
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroPredisposizioniIncomplete">di cui incompleti</label>
									<div class="controls">
										<input type="text" value="" class="span1 soloNumeri" readonly="readonly" id="numeroPredisposizioniIncomplete">
										<span class="al">
											<label class="radio inline" for="importoPredisposizioniIncomplete">Totale euro</label>
										</span>
										<input type="text" value="" class="span2 soloNumeri" readonly="readonly" id="importoPredisposizioniIncomplete">									
									</div>							
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroPredisposizioniDefinite">definiti </label>
									<div class="controls">
										<input type="text" value="" class="span1 soloNumeri" readonly="readonly" id="numeroPredisposizioniDefinite">
										<span class="al">
											<label class="radio inline" for="importoPredisposizioniDefinite">Totale euro</label>
										</span>
										<input type="text" value="" class="span2 soloNumeri" readonly="readonly" id="importoPredisposizioniDefinite">									
									</div>							
								</div>
							</fieldset>
						</div>						
						<h4 class="step-pane active">Impegno<span id="SPAN_impegnoH4"></span></h4>
						<fieldset class="form-horizontal" id="fieldsetImpegno">
							<div class="control-group">
								<label class="control-label" for="annoMovimento">Anno *</label>
								<div class="controls">
									<input type="text" required value="" class="span1 soloNumeri" id="annoMovimento" name="movimentoGestione.annoMovimento">
									<span class="al">
										<label class="radio inline" for="numeroMovimentoGestione">Numero *</label>
									</span>
									<input type="text" required value="" class="span2 soloNumeri" id="numeroMovimentoGestione" name="movimentoGestione.numero">
									<span class="al">
										<label class="radio inline" for="numeroSubmovimentoGestione">Sub</label>
									</span>
									<input type="text" value="" class="span2 soloNumeri" id="numeroSubmovimentoGestione" name="subMovimentoGestione.numero">
									
									<span class="radio guidata">
								 		<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
									</span>	
									
									<div class="radio inline collapse_alert hide" id="divDisponibilitaMovimentoGestione">
										<span class="icon-chevron-right icon-red alRight"></span>Disponibile <span id="disponibilitaMovimentoGestione"></span>
									</div>										
								</div>
							</div>
						</fieldset>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<s:if test="!hasInformazioni()">
							<span class="pull-right">
								<s:submit cssClass="btn btn-primary" value="completa e definisci" />
							</span>
						</s:if>
					</p>
				</s:form>
				<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/definisciElencoSpesa.js"></script>
</body>
</html>