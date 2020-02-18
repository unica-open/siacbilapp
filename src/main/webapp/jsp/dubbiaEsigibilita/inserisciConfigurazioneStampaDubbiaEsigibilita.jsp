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
							<div class="control-group">
								<label class="control-label" for="accantonamento">Anno esercizio</label>
								<div class="controls">
									<input type="text" disabled="disabled" value='<s:property value="%{annoEsercizioInt}" />' />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="accantonamento">Accantonamento graduale enti locali *</label>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" name="accantonamentoEnti" value="true" <s:if test='%{attributiBilancio.accantonamentoGraduale}'>checked</s:if> />S&igrave;
									</label>
									<label class="radio inline">
										<input type="radio" name="accantonamentoEnti" value="false" <s:if test='%{!attributiBilancio.accantonamentoGraduale}'>checked</s:if> />No
									</label>
									<s:if test="attributiBilancioPresenti">
										<s:hidden name="attributiBilancio.accantonamentoGraduale" />
									</s:if>
								</div>
							</div>
							<%-- riepilogo --%>

							<table summary="riepilogo incarichi" class="table table-hover table-bordered">
								<tr>
									<th width="20%">&nbsp;</th>
									<th width="27%" class="text-right"><s:property value="%{annoEsercizioInt + 0}" /></th>
									<th width="27%" class="text-right"><s:property value="%{annoEsercizioInt + 1}" /></th>
									<th width="26%" class="text-right"><s:property value="%{annoEsercizioInt + 2}" /></th>
								</tr>
								<tr>
									<th>Accantonamento graduale *</th>
									<td class="text-right tab_Right"><s:textfield id="valoreComplessivoProgetto" cssClass=" span6 soloNumeri decimale" name="attributiBilancio.percentualeAccantonamentoAnno" maxlength="20" /></td>
									<td class="text-right tab_Right"><s:textfield id="valoreComplessivoProgetto" cssClass=" span6 soloNumeri decimale" name="attributiBilancio.percentualeAccantonamentoAnno1" maxlength="20" /></td>
									<td class="text-right tab_Right"><s:textfield id="valoreComplessivoProgetto" cssClass=" span6 soloNumeri decimale" name="attributiBilancio.percentualeAccantonamentoAnno2" maxlength="20" /></td>
								</tr>
							</table>

							<div class="control-group">
								<label class="control-label" for="accantonamento">Riscossione virtuosa *</label>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" id="riscossioneVirtuosaTrue" <s:if test="attributiBilancioPresenti">disabled</s:if> name="riscossioneVirtuosa" value="true" <s:if test='%{attributiBilancio.riscossioneVirtuosa}'>checked</s:if> />S&igrave;
									</label>
									<label class="radio inline">
										<input type="radio"  id="riscossioneVirtuosaFalse" <s:if test="attributiBilancioPresenti">disabled</s:if> name="riscossioneVirtuosa" value="false" <s:if test='%{! attributiBilancio.riscossioneVirtuosa}'>checked</s:if> />No
									</label>
									<s:if test="attributiBilancioPresenti">
										<s:hidden name="attributiBilancio.riscossioneVirtuosa" />
									</s:if>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="accantonamento">Tipo media prescelta *</label>
								<div class="controls">
									<label class="radio inline">
										<input type="radio"  id="mediaApplicataSemplice" <s:if test="attributiBilancioPresenti">disabled</s:if> name="mediaApplicata" value="SEMPLICE" <s:if test='%{"SEMPLICE".equals(attributiBilancio.mediaApplicata.name())}'>checked</s:if> />Semplice
									</label>
									<label class="radio inline">
										<input type="radio"   id="mediaApplicataPonderata" <s:if test="attributiBilancioPresenti">disabled</s:if> name="mediaApplicata" value="PONDERATA" <s:if test='%{"PONDERATA".equals(attributiBilancio.mediaApplicata.name())}'>checked</s:if> />Ponderata
									</label>
									<s:if test="attributiBilancioPresenti">
										<s:hidden name="attributiBilancio.mediaApplicata" />
									</s:if>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="valoreComplessivoProgetto">Ultimo bilancio Approvato (UBA) *</label>
								<div class="controls">
									<s:textfield id="valoreComplessivoProgetto" readonly="attributiBilancioPresenti" cssClass="lbTextSmall span3 soloNumeri" name="attributiBilancio.ultimoAnnoApprovato" maxlength="20" />
								</div>
							</div>

							<p class="margin-large">

								<s:if test="attributiBilancioPresenti">
									<span id="SPAN_pulsanteApriModaleCapitoloDubbiaEsigibilita" class="radio guidata">
										<a href="#" id="pulsanteApriModaleCapitoloDubbiaEsigibilita" class="btn btn-primary">ricerca capitoli</a>
									</span>
									<span class="radio guidata">
										<s:submit cssClass="btn btn-primary pull-right" value="aggiorna Parametri FCDE" />
									</span>
								</s:if>
								<s:else>
									<s:submit cssClass="btn btn-primary pull-right" value="salva e prosegui" />
								</s:else>
								<s:if test="datiAnnoPrecedentePresenti">
									<span class="radio guidata">
										<button type="button" class="btn btn-secondary" id="pulsantePopolaDaAnnoPrecedente" data-copia-precedente>copia Capitoli da Bilancio Previsione</button>
									</span>
								</s:if>
								<s:if test="datiAnnoPrecedenteGestionePresenti">
									<span class="radio guidata">
										<button type="button" class="btn btn-secondary" id="pulsantePopolaDaAnnoPrecedenteGestione" data-copia-precedente>copia Capitoli da Bilancio Gestione</button>
									</span>
								</s:if>
							</p>

							<br/>
							<br/>
							<br/>
							<%-- tab temp capitoli --%>
							<div id="tempCapitoli" class="hide" role="grid">
								<h4>Capitoli da associare all'accantonamento</h4>
								<div class="dataTable-scroll-x">
									<table class="table table-hover dataTable" id="tabellaTempCapitoli">
										<thead>
											<tr>
												<th width="5%">
													<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti nella pagina corrente" value ="" data-referred-table="#tabellaTempCapitoli" />
												</th>
												<th width="25%">Capitolo</th>
												<th width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 4}" /></th>
												<th width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 3}" /></th>
												<th width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 2}" /></th>
												<th width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 1}" /></th>
												<th width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato}" /></th>
												<th width="10%" class="text-right">&nbsp;&#37; MEDIA</th>
												<th width="10" class="text-right">&nbsp;&#37; con delta</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
								<span class="radio guidata "> <a href="#"
									id="salvaCapitoli" class="btn btn-primary disabled">salva capitoli</a>
								</span>
								<span id="SPAN_pulsanteRiportaSuCapitoli" class="radio guidata ">
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
											<th scope="col" width="24%">Capitolo</th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 4}" /></th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 3}" /></th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 2}" /></th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 1}" /></th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato}" /></th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; MEDIA</th>
											<th scope="col" width="10%" class="text-right">&nbsp;&#37; con delta</th>
											<th scope="col" width="3%" class="text-right">&nbsp;</th>
											<th scope="col" width="3%" class="text-right">&nbsp;</th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
							</div>
						</div>
						<div class="Border_line"></div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<a class="btn btn-secondary" href="inserisciConfigurazioneStampaDubbiaEsigibilita_annulla.do">annulla</a>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/dubbiaEsigibilita/selezionaCapitoloDubbiaEsigibilita_modale.jsp" />
	<s:include value="/jsp/dubbiaEsigibilita/aggiornaAccantonamentoDubbiaEsigibilita_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />


	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />


	<script type="text/javascript" src="${jspath}capitolo/capitolo.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/capitoloEntrata.js"></script>
	<script type="text/javascript" src="${jspath}async.js"></script>
	<script type="text/javascript" src="${jspath}dubbiaEsigibilita/inserisciConfigurazioneStampaDubbiaEsigibilita.js"></script>


</body>
</html>