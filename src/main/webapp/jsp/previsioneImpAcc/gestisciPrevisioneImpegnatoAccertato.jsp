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
					
					<h3>Previsione a Chiudere Impegnato/Accertato</h3>
					<%-- <s:form method="post" action="ricercaPrevisioneImpegnatoAccertato_ricercaCapitoli" novalidate="novalidate"> --%>
							<!-- <div class="fieldset-group">
								<div class="fieldset-heading">
									<h4>Ricerca capitolo</h4>
									<p>&Egrave; necessario inserire il tipo capitolo (entrata/spesa)</p>
									<br/>
								</div> -->
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle" href="#collapseRicerca" data-parent="#accordion2" data-toggle="collapse">
											Ricerca Capitolo<span class="icon"></span>
										</a>
									</div>
									<div id="collapseRicerca" class="accordion-body in collapse" style="height: auto;">
										<div class="accordion-inner step-content">
											<p>&Egrave; necessario inserire almeno il tipo capitolo (entrata/spesa)</p>
											<fieldset id="fieldsetRicerca" class="form-horizontal">
												<s:hidden name="nomeAzioneSAC" id="nomeAzioneSAC" />
												<s:hidden name="forzaRicerca" id="HIDDEN_forzaRicerca" />
												<div class="control-group">
													<label class="control-label" for="tipoCapitolo">Tipo *</label>
													<div class="controls">
														<label class="radio inline">
															<input type="radio" name="capitolo.tipoCapitolo" id="tipoCapitoloRadioEntrata" value="CAPITOLO_ENTRATA_GESTIONE"
															<s:if test='%{"CAPITOLO_ENTRATA_GESTIONE".equals(capitolo.tipoCapitolo.name())}'>
																	checked
															</s:if>
															 
															>&nbsp;Entrata
														</label>
														<label class="radio inline">
															<input type="radio" name="capitolo.tipoCapitolo" id="tipoCapitoloRadioSpesa" value="CAPITOLO_USCITA_GESTIONE" 
															<s:if test='%{"CAPITOLO_USCITA_GESTIONE".equals(capitolo.tipoCapitolo.name())}'>
																	checked
															</s:if>
															>&nbsp;Uscita
														</label>
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="numeroCapitolo">Capitolo</label>
													<div class="controls">
														<input type="text" id="numeroCapitolo" class="lbTextSmall span2 soloNumeri" 
															name="capitolo.numeroCapitolo" maxlength="200" 
															placeholder="capitolo" value="${capitolo.numeroCapitolo}" />
														<span class="al"> 
															<label class="radio inline" for="numeroArticolo">Articolo</label>
														</span> 
														<input type="text" id="numeroArticolo" class="lbTextSmall span2 soloNumeri" 
															name="capitolo.numeroArticolo" maxlength="200"
															placeholder="articolo" value="${capitolo.numeroArticolo}" />
													</div>
												</div>
												<div class="control-group">
													<label for="bottoneSAC" class="control-label">
														Struttura Amministrativa Responsabile 
													</label>
													<div class="controls">
														<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" data-original-uid="%{strutturaAmministrativoContabile.uid}" />
														<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />
														<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoResponsabileCodice" />
														<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceTipoClassificatore" name="codiceTipoClassificatoreStrutturaAmministrativoContabile" />
														<s:hidden id="HIDDEN_StrutturaAmministrativoContabileEditabile" name="strutturaAmministrativoContabileEditabile" value="true"/>
														<a href="#struttAmm" role="button" class="btn" id="bottoneSAC" data-toggle="modal">
															Seleziona la Struttura amministrativa &nbsp;
															<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
														</a>
														&nbsp;
														<span id="SPAN_StrutturaAmministrativoContabile">
															<s:if test="%{strutturaAmministrativoResponsabileCodice != null}">
																<s:property value="strutturaAmministrativoResponsabileCodice"/>
															</s:if><s:else>
																Nessuna Struttura Amministrativa Responsabile selezionata 
															</s:else>
														</span>
													</div>
												</div>
												<p>
													<s:include value="/jsp/include/indietro.jsp" />
													<button  id="pulisciCampiRicerca" type="button" class="btn reset">annulla</button>
													<button id="cercaCapitoliPerPrevisione" type="button" class="btn btn-primary pull-right"> cerca</button>
												</p>												
												<%-- <s:submit cssClass="btn btn-primary pull-right" value="cerca"/> --%>
											</fieldset>
										</div> <!-- CHIUDO ACCORDION INNER -->
										
									</div>  <!-- CHIUDO COLLAPSE -->
								</div>
								<div class="step-content">
								<div class="fieldset">
									<div class="fieldset-body">
										<%-- TABELLA CAPITOLI --%>
										<div id="risultatiDiRicercaCapitoli" class="hide">
											<h3><span id="id_num_result" class="num_result"></span> Risultati trovati</h3>
											<div id="tabellaUscita">
												<s:include value="/jsp/previsioneImpAcc/tabellaRisultatiRicerca.jsp">
													<s:param name="suffix">UscitaGestione</s:param>
												</s:include>
											</div>
											<div id="tabellaEntrata">
												<s:include value="/jsp/previsioneImpAcc/tabellaRisultatiRicerca.jsp">
													<s:param name="suffix">EntrataGestione</s:param>
												</s:include>
											</div>
											<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
										</div>
										<%-- TABELLA CAPITOLI --%>
									</div>
							    </div>
						
						<div id="myModal" class="modal hide fade" tabindex="-1"
							role="dialog" aria-labelledby="myModalLabel"
							aria-hidden="true">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">×</button>
								<h3 id="myModalLabel">Piano dei Conti</h3>
							</div>
							<div class="modal-body">
								<ul id="treePDC" class="ztree"></ul>
							</div>
							<div class="modal-footer">
								<button id="deselezionaElementoPianoDeiConti" class="btn">Deseleziona</button>
								<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
							</div>
						</div>
						<div id="struttAmm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
								<h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
							</div>
							<div class="modal-body">
								<ul id="treeStruttAmm" class="ztree"></ul>
							</div>
							<div class="modal-footer">
								<button id="deselezionaStrutturaAmministrativoContabile" class="btn">Deseleziona</button>
								<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
							</div>
						</div>
						
						<div id="msgPrevisioneImpegnatoAccertatoLabel" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrevisioneImpegnatoAccertatoLabel" aria-hidden="true">
							<div class="modal-body">
								<h4>Previsione <span id="descrizionePrevisioneImpAcc"></span></h4>
								<div class="alert alert-error hide" id="ERRORI_modale">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<strong>Attenzione!!</strong><br>
									<ul></ul>
								</div>
								<div class="row-fluid">
									<div class="control-group">
										<div id="tabellaImportiCapitolo" class="controls">
											<button type="button" class="close" data-hide="alert">&times;</button>
											<strong>Attenzione!!</strong><br>
											<ul></ul>	
										</div>
									</div>
								</div>			
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
								<!-- <button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaCapUscitaGestioneElimina.do">si, prosegui</button> -->
								<button class="btn btn-primary" id="pulsanteAggiornaPrevisioneImpegnatoAccertato">salva previsione</button>
							</div>
						</div>
					<%-- </s:form> --%>

					</div>
				</div> <!-- chiudo content page -->
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/capitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/risultatiRicercaMulti.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/previsioneImpAcc/gestisciPrevisioneImpegnatoAccertato.js"></script>
</body>
</html>