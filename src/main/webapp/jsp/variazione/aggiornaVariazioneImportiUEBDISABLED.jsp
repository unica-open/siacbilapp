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
				
				<s:form cssClass="form-horizontal" action="concludiAggiornamentoVariazioneImporti" novalidate="novalidate" id="aggiornaVariazioneImportiConUEB" method="post">
					<h3>Aggiorna Variazione</h3>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" href="#collapseVariazioni" data-parent="#accordion2" data-toggle="collapse">
								Variazione<span class="icon"></span>
							</a>
						</div>
						<div id="collapseVariazioni" class="accordion-body in collapse" style="height: auto;">
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
									<dt>Anno di competenza</dt>
									<dd>&nbsp;<s:property value="annoCompetenza" /></dd>
								</dl>
								<h5>Elenco modifiche in variazione</h5>    
								<table class="table table-condensed table-hover table-bordered" id="tabellaGestioneVariazioni" summary="....">
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col" class="text-center">Competenza</th>
											<th scope="col" class="text-center">Residuo</th>
											<th scope="col" class="text-center">Cassa</th>
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

										</tr>
										<tr class="info">
											<th>Totale spese</th>
											<td><span id="totaleSpeseCompetenzaVariazione"></span></td>
											<td><span id="totaleSpeseResiduoVariazione"></span></td>
											<td><span id="totaleSpeseCassaVariazione"></span></td>
										</tr>
										<tr class="info">
											<th>Differenza</th>
											<td><span id="differenzaCompetenzaVariazione"></span></td>
											<td><span id="differenzaResiduoVariazione"></span></td>
											<td><span id="differenzaCassaVariazione"></span></td>
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
										<%-- <div id="collapseProvvedimento" class="accordion-body collapse">
											<div class="accordion-inner">
												<!-- <p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p> -->
												<s:include value="/jsp/provvedimento/formRicercaProvvedimentoDISABLED.jsp" />
											</div>
										</div> --%>
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
								<p class="margin-large">
									<s:include value="/jsp/include/indietro.jsp" />
									&nbsp;<span class="nascosto"> | </span>
									<s:if test="salvaAbilitato">
										<button id="aggiornaVariazioneButton" type='button' id="" class="btn" >salva variazione</button>
										&nbsp;<span class="nascosto"> | </span>
									</s:if>
									<s:if test="annullaAbilitato">
										<button id="annullaVariazioneButton" type='button' id="" class="btn" >annulla variazione</button>
										&nbsp;<span class="nascosto"> | </span>
									</s:if>
									<s:if test="concludiAbilitato">
										<button id="concludiVariazioneButton" type="button" class="btn btn-primary pull-right">concludi attivit&agrave;</button>
									</s:if>
								</p>
								
							</div>
						</div>
					</div>
					<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
						<div class="modal-body">
							<div class="alert alert-error alert-persistent">
								<p>
									<strong>Attenzione!!!</strong>
								</p>
								<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
							<button type="button" class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>

	<div id="iframeContainer"></div>
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztreeSAC.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script> 
	<script type="text/javascript" src="${jspath}variazioni/variazioni.js"></script> 
	<script type="text/javascript" src="${jspath}variazioni/aggiorna.importi.ueb.js"></script>

</body>
</html>