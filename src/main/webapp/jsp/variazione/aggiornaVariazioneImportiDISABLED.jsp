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
				<s:form cssClass="form-horizontal" action="concludiAggiornamentoVariazioneImporti" novalidate="novalidate" method="post">
					<h3>Aggiorna Variazione</h3>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" href="#collapseVariazioni" data-parent="#accordion2" data-toggle="collapse">
								Variazione<span class="icon"></span>
							</a>
						</div>
						<div id="collapseVariazioni" class="accordion-body in collapse overlay-on-submit" style="height: auto;">
							<div class="accordion-inner">
								<dl class="dl-horizontal">
									<dt>Num. variazione</dt>
									<dd>&nbsp;<s:property value="numeroVariazione" /></dd>
									<dt>Stato</dt>
									<dd>&nbsp;<s:property value="elementoStatoOperativoVariazione.descrizione" /></dd>
									<dt>Applicazione</dt>
									<dd>&nbsp;<s:property value="applicazione" /></dd>
									<dt>Tipo Variazione</dt>
									<dd>&nbsp;<s:property value="tipoVariazione.codice" />&nbsp;-&nbsp;<s:property value="tipoVariazione.descrizione" /></dd>
									<!-- SIAC 6884 -->
									<s:if test="%{model.isDecentrata}">
										<dt>Data Apertura Proposta</dt>
										<dd>&nbsp;<s:property value="dataAperturaFormatted" /></dd>
										<dt>Data Chiusura Proposta</dt>
										<dd>&nbsp;<s:property value="dataChiusuraFormatted" /></dd>
										<dt>Direzione Proponente</dt>
										<dd>&nbsp;<s:property value="direzioneProponente.codice" />&nbsp;-&nbsp;<s:property value="direzioneProponente.descrizione" /></dd>
									</s:if>
									
									
								</dl>
								<h5>Elenco modifiche in variazione</h5>    
								<table class="table table-condensed table-hover table-bordered" id="tabellaGestioneVariazioni" summary="....">
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Residuo ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Cassa ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 1}</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 2}</th>
											<th scope="col">&nbsp;</th>
											<th scope="col">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr class="info">
											<th>Totale entrate</th>
											<td><span id="totaleEntrateCompetenzaVariazione"></span></td>
											<td><span id="totaleEntrateResiduoVariazione"></span></td>
											<td><span id="totaleEntrateCassaVariazione"></span></td>
											<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuDue"></span></td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr class="info">
											<th>Totale spese</th>
											<td><span id="totaleSpeseCompetenzaVariazione"></span></td>
											<td><span id="totaleSpeseResiduoVariazione"></span></td>
											<td><span id="totaleSpeseCassaVariazione"></span></td>
											<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuDue"></span></td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr class="info">
											<th>Differenza</th>
											<td><span id="differenzaCompetenzaVariazione"></span></td>
											<td><span id="differenzaResiduoVariazione"></span></td>
											<td><span id="differenzaCassaVariazione"></span></td>
											<td><span id="differenzaCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="differenzaCompetenzaVariazioneAnnoPiuDue"></span></td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
									</tfoot>
								</table>
								<div id="divEsportazioneDati" class="form-horizontal">
									<button id="pulsanteEsportaDati" type="submit" class="pull-left btn btn-secondary">
										Esporta capitoli in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
									<button id="pulsanteEsportaDatiXlsx" type="submit" class="pull-left btn btn-secondary">
										Esporta capitoli in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
								</div>
								<br/>
								<h5>Aggiorna le note e/o la descrizione</h5>
								<div class="control-group">
									<label class="control-label" for="descrizioneVariazione">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneVariazione" placeholder="descrizione" cssClass="span10" name="descrizione" maxlength="500" required="true" disabled="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="noteVariazione" class="control-label">Note</label>
									<div class="controls">
										<s:textarea rows="2" cols="55" cssClass="span10" id="noteVariazione" name="note" maxlength="500" disabled="true"></s:textarea>
									</div>
								</div>
								
								<div class="accordion margin-large" id="accordion5">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle disabled collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
												<s:property value="stringaProvvedimento"/>
											</a>
										</div>										
									</div>
								</div>
								<div class="accordion margin-large" id="accordion5">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle disabled collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
												<s:property value="stringaProvvedimentoAggiuntivo"/>
											</a>
										</div>										
									</div>
								</div>
								
								<p>&nbsp;</p>
								<s:if test="fromInserimento">
									<p id="bottoniInserimento" class="margin-large">
										<s:a cssClass="btn" action="redirectToCruscotto" id="pulsanteRedirezioneCruscotto">Indietro</s:a>
										<s:if test="salvaAbilitato">
											<button type="button" id="aggiornaVariazioneButton" title='salva variazione' role='button' class="btn">&nbsp;salva&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_salva" data-spinner-async></i></button>
										</s:if>
									</p>
								</s:if><s:else>
									<p class="margin-large">
										<s:include value="/jsp/include/indietro.jsp" />
										&nbsp;<span class="nascosto"> | </span>
										<s:if test="salvaAbilitato">
											<%--<s:submit action="salvaAggiornamentoVariazioneImporti" cssClass="btn" id="aggiornaVariazioneButton" value="salva" /> --%>
											<button type="button" id="aggiornaVariazioneButton" title='salva variazione' role='button' class="btn">&nbsp;salva&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_salva" data-spinner-async></i></button>
											&nbsp;<span class="nascosto"> | </span>
										</s:if>
										<s:if test="annullaAbilitato">
											<button id = "annullaVariazioneButton" type="button" title='annulla variazione' role='button' class="btn">annulla variazione</button>
											&nbsp;<span class="nascosto"> | </span>
										</s:if>
										<s:if test="concludiAbilitato">
											<s:if test="variazioneDecentrataAperta && dataChiusuraProposta == null">
												<button type = "button" id="chiudiPropostaButton" type="button" title='chiudi proposta' role='button' class="btn btn-primary pull-right">&nbsp;chiudi proposta<i class="icon-spin icon-refresh spinner" id="spinner_concludi" data-spinner-async></i></button>
											</s:if>
											<s:else>
											<button id="concludiVariazioneButton" type="button" title='concludi attività variazione' role='button' class="btn btn-primary pull-right">&nbsp;concludi attività&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_concludi" data-spinner-async></i></button>
											</s:else>
										</s:if>
									</p>
								</s:else>
							</div>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Modale annulla variazione --%>
	<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		<div class="modal-body">
			<div class="alert alert-error">
				<p>
					<strong>Attenzione!!!</strong>
				</p>
				<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
			<button class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
		</div>
	</div>
	<%-- /Modale annulla --%>
	
	<s:hidden id="redirectAction" value="redirectToDisabledAggiornamentoVariazioneImporti"/>

	<div id="iframeContainer"></div>
	<s:include value="/jsp/include/footer.jsp"/>
	<input type="hidden" id="DISABLED" />
	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/capitolo.variazione.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/aggiorna.importi.js"></script>

</body>
</html>