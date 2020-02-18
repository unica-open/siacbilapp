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
					<s:form  method="post" action="%{actionOperazioneAttributi}">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="gestioneUEB" id="HIDDEN_gestioneUEB" data-maintain="" />
						<h3>Configurazione FCDE</h3>
						<fieldset class="form-horizontal"> 
							<br />
							<s:hidden name="attributiBilancio.uid" />
							<s:hidden name="attributiBilancio.accantonamentoGraduale" data-maintain="" />
							<s:hidden name="attributiBilancio.percentualeAccantonamentoAnno" data-maintain="" />
							<s:hidden name="attributiBilancio.percentualeAccantonamentoAnno1" data-maintain="" />
							<s:hidden name="attributiBilancio.percentualeAccantonamentoAnno2" data-maintain="" />
							<s:hidden name="attributiBilancio.riscossioneVirtuosa" />
							<s:if test="attributiBilancioPresenti">
								<s:hidden name="attributiBilancio.mediaApplicata" />
								<s:hidden name="attributiBilancio.ultimoAnnoApprovato" />
							</s:if>
							
							<div class="control-group">
								<label class="control-label" for="accantonamento">Anno esercizio</label>
								<div class="controls">
									<s:textfield name="annoEsercizio" disabled="true" data-maintain="" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">Tipo media prescelta *</label>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" id="mediaApplicataSemplice" <s:if test="attributiBilancioPresenti">disabled</s:if> name="attributiBilancio.mediaApplicata" value="SEMPLICE"
											<s:if test='%{"SEMPLICE".equals(attributiBilancio.mediaApplicata.name())}'>checked</s:if> />Semplice
									</label>
									<label class="radio inline">
										<input type="radio" id="mediaApplicataPonderata" <s:if test="attributiBilancioPresenti">disabled</s:if> name="attributiBilancio.mediaApplicata" value="PONDERATA"
											<s:if test='%{"PONDERATA".equals(attributiBilancio.mediaApplicata.name())}'>checked</s:if> />Ponderata
									</label>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="ultimoAnnoApprovato">Ultimo bilancio Approvato (UBA) *</label>
								<div class="controls">
									<s:textfield id="valoreComplessivoProgetto" disabled="attributiBilancioPresenti" cssClass="lbTextSmall span3 soloNumeri" name="attributiBilancio.ultimoAnnoApprovato" maxlength="4" />
								</div>
							</div>

							<p class="margin-large">
								<s:if test="attributiBilancioPresenti">
									<span id="SPAN_pulsanteApriModaleCapitoloDubbiaEsigibilita" class="radio guidata">
										<button type="button" class="btn btn-primary" id="pulsanteApriModaleCapitoloDubbiaEsigibilita">ricerca capitoli</button>
									</span>
									<span class="radio guidata">
										<s:submit cssClass="btn btn-primary pull-right" value="Aggiorna Parametri FCDE" />
									</span>
								</s:if>
								<s:else>
									<s:submit cssClass="btn btn-primary pull-right" value="salva e prosegui" />
								</s:else>
								<s:if test="datiAnnoPrecedentePresenti">
									<span class="radio guidata">
										<button type="button" class="btn btn-secondary" id="pulsantePopolaDaAnnoPrecedente">copia Capitoli da Bilancio di Previsione</button>
									</span>
								</s:if>
							</p>

							<br/>
							<br/>
							<br/>
							<div id="tempCapitoli" class="hide" role="grid">
								<h4>Capitoli da associare all'accantonamento</h4>
								<div class="dataTable-scroll-x">
									<table class="table table-hover dataTable" id="tabellaTempCapitoli">
										<thead>
											<tr>
												<th class="span1">
													<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti nella pagina corrente" value ="" data-referred-table="#tabellaTempCapitoli" />
												</th>
												<th class="span5">Capitolo</th>
												<th class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 4}" /></th>
												<th class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 3}" /></th>
												<th class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 2}" /></th>
												<th class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 1}" /></th>
												<th class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato}" /></th>
												<th class="span1 text-right">&nbsp;&#37; MEDIA</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
								<span class="radio guidata ">
									<button type="button" id="salvaCapitoli" class="btn btn-primary disabled">salva capitoli</button>
								</span>
								<span id="SPAN_pulsanteRiportaSuCapitoli" class="radio guidata">
									<button type="button" id="pulsanteRiportaSuCapitoli" disabled="disabled" class="btn btn-primary">riporta su tutti i capitoli</button>
								</span>
							</div>
						</fieldset>
						<br/>
						<div>
							<h4>Capitoli nell'accantonamento</h4>
							<div class="dataTable-scroll-x">
								<table id="riepilogoCapitoliGiaAssociati" class="table table-hover dataTable">
									<thead>
										<tr role="row">
											<th scope="col" class="span4">Capitolo</th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 4}" /></th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 3}" /></th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 2}" /></th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 1}" /></th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato}" /></th>
											<th scope="col" class="span1 text-right">&nbsp;&#37; MEDIA</th>
											<th scope="col" class="span1 text-right">&nbsp;</th>
											<th scope="col" class="span1 text-right">&nbsp;</th>
										</tr>
									</thead>
									<tbody></tbody>	
								</table>
							</div>
						</div>
						<div class="Border_line"></div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/dubbiaEsigibilita/selezionaCapitoloDubbiaEsigibilita_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

	<script type="text/javascript" src="${jspath}capitolo/capitolo.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="${jspath}async.js"></script>
	<script type="text/javascript" src="${jspath}dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto.js"></script>


</body>
</html>