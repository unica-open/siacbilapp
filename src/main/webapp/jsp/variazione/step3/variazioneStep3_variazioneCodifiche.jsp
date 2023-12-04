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
				<s:include value="/jsp/include/messaggi.jsp" />
 				<h3>Inserisci Variazione</h3>
				<div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Scegli<span class="chevron"></span></li>
						<li data-target="#step2" class="complete"><span class="badge badge-success">2</span>Definisci<span class="chevron"></span></li>
						<li data-target="#step3" class="active"><span class="badge">3</span>Specifica<span class="chevron"></span></li>
						<li data-target="#step4"><span class="badge">4</span>Riepilogo<span class="chevron"></span></li>
					</ul>
				</div>
				<s:form cssClass="form-horizontal" action="esecuzioneStep3InserimentoVariazioniCodifiche" novalidate="novalidate" id="formVariazioneStep3_VariazioneCodifiche" method="post">
					<input type="hidden" id="tipoAzione" value="Inserimento" data-maintain=""/>
					<div class="step-content">
						<div class="step-pane active" id="step3">
							<fieldset class="form-horizontal">
								<div class="control-group">
									<span class="control-label">Capitolo</span>
									<div class="controls">
										<label class="radio inline">
											<input type="radio" name="specificaCodifiche.tipoCapitolo" id="tipoCapitoloRadioEntrata" value="Entrata" <s:if test='%{specificaCodifiche.tipoCapitolo eq "Entrata"}'>checked="checked"</s:if>>&nbsp;Entrata
										</label>
										<label class="radio inline">
											<input type="radio" name="specificaCodifiche.tipoCapitolo" id="tipoCapitoloRadioUscita" value="Uscita" <s:if test='%{specificaCodifiche.tipoCapitolo eq "Uscita"}'>checked="checked"</s:if>>&nbsp;Spesa
										</label>
										<s:hidden name="definisci.applicazione" id="HIDDEN_tipoApplicazione" data-maintain="" />
										<s:hidden name="annoEsercizioInt" id="HIDDEN_annoEsercizio" data-maintain="" />
										<s:hidden name="definisci.annoVariazione" id="HIDDEN_annoVariazione" data-maintain="" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="annoCapitolo">Anno</label>
									<div class="controls">
										<s:textfield id="annoCapitolo" cssClass="lbTextSmall span2" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
										<s:hidden name="specificaCodifiche.annoCapitolo" data-maintain="" />
										<span class="al">
											<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
										</span>
										<s:textfield id="numeroCapitolo" cssClass="lbTextSmall span2" name="specificaCodifiche.numeroCapitolo" required="true" maxlength="9" />
										<span class="al">
											<label class="radio inline" for="numeroArticolo">Articolo *</label>
										</span>
										<s:textfield id="numeroArticolo" cssClass="lbTextSmall span2" name="specificaCodifiche.numeroArticolo" required="true" maxlength="9" />
										<a class="btn btn-primary" href="#" id="BUTTON_ricercaCapitolo">
											<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
										</a>
									</div>
								</div>
							</fieldset>
							<div id="divRicercaCapitolo" class="hide">
								<h4>Risultati della ricerca</h4>
								<div class="box-border" id="risultatiRicercaCapitolo">
									<dl class="dl-horizontal-inline">
										<dt>Anno:</dt>
										<dd><span id="annoCapitoloTrovato"></span>&nbsp;</dd>
										<dt>Capitolo / Articolo:</dt>
										<dd><span id="numeroCapitoloArticoloTrovato"></span>&nbsp;</dd><%-- 300 / 1 --%>
									</dl>
									
									<div class="collapse-group">
										<div class="pull-centered margin-large">
											<a class="btn btn-collapse pull-center" data-toggle="collapse" data-parent="#risultatiRicercaCapitolo" href="#collapseGestioneCodifiche" id="linkGestioneCodifiche">
												<i class="icon-plus-sign icon-2x"></i>&nbsp;Gestione codifiche
											</a>
										</div>
									
										<div id="collapseGestioneCodifiche" class="collapse-body collapse">
											<div class="collapse-inner">
											
											<!----------------- CAPITOLO USCITA ---------->
												<s:include value="/jsp/variazione/step3/variazioneStep3_variazioneCodifiche_capitoloUscita.jsp"/>			
											
											<!---CAPITOLO ENTRATA ------------------->
												<s:include value="/jsp/variazione/step3/variazioneStep3_variazioneCodifiche_capitoloEntrata.jsp"/>
 											
 												<a href="#" class="btn" id="bottoneInserisciModifica">
													inserisci modifica&nbsp;
													<i class="icon-spin icon-refresh spinner" id="inserisciModificaSpinner"></i>
												</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div id="divModifiche">
								<h4>Elenco modifiche</h4>
								<table class="table table-hover table-bordered" id="codificheNellaVariazione" summary="...." >
									<thead>
										<tr>
											<th scope="col">&nbsp;</th>
											<th scope="col">Capitolo</th>
											<th scope="col">Descrizione capitolo</th>
											<th scope="col">Descrizione articolo</th>
											<th scope="col">Struttura amministrativa</th>							
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<h5>Inserisci note a corredo della variazioni</h5>
								<div class="control-group">
									<label for="noteVariazione" class="control-label">Note</label>
									<div class="controls">
										<s:textarea rows="2" cols="65" cssClass="span10" id="noteVariazione" name="specificaCodifiche.note" maxlength="500"></s:textarea>
									</div>
								</div>
								<p>
									<a class="btn" role="button" data-toggle="modal" id="pulsanteEliminaCodifica" href="#modaleEliminazione">elimina modifica</a>
									<a class="btn" id="pulsanteNuovaCodifica">nuova ricerca</a>
									<a class="btn" id="pulsanteAggiornaCodifica">
										aggiorna modifica
										<i class="icon-spin icon-refresh spinner" id="pulsanteAggiornamentoSpinner"></i>
									</a>
								</p>
							</div>
							
							<p>
								<s:include value="/jsp/include/indietro.jsp"/>
								<button type="button" class="btn btn-link reset">annulla</button>
								<s:submit cssClass="btn btn-primary pull-right" value="salva e prosegui"/>
							</p>
						</div>
					</div>
				</s:form>
				
				<s:include value="/jsp/variazione/step3/include/modaleCompilazioneGuidataSIOPEVariazione_capEntrata.jsp"/>
							
				<s:include value="/jsp/variazione/step3/include/modaleCompilazioneGuidataSIOPEVariazione_capSpesa.jsp"/>								
			</div>
		</div>
	</div>
	
	<%-- Modale eliminazione --%>
	<div aria-hidden="false" aria-labelledby="modaleEliminazioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleEliminazione">
		<div class="modal-body">
			<div class="alert alert-error alert-persistent">
				<button data-hide="alert" class="close" type="button">×</button>
				<p><strong>Attenzione!!!</strong></p>
				<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn" id="modaleEliminazionePulsanteCancella">no, indietro</button>
			<button class="btn btn-primary" id="modaleEliminazionePulsanteProsegui">
				si, prosegui
				<i class="icon-spin icon-refresh spinner" id="modaleEliminazioneSpinner"></i>
			</button>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaSIOPE.js"></script>	
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/classificatori.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.step3.codifiche.js"></script>
	
</body>
</html>