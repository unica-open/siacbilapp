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
	
	
	
		<%-- Pagina JSP vera e propria  --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3>Stanziamenti</h3>
				<div class="step-content">
					<div class="step-pane active" >
						<fieldset id="fieldset_aggiornamento_componenti">
						
							<div class="control-group">
								<button type="button" class="btn" id="button_inserisciModifica" style="margin-bottom:10px;">Nuova Riga Componente</button>
							</div>
							<s:include value="/jsp/cap/include/tabella_stanziamenti_con_componenti_spesa.jsp">
								<s:param name="visualizzaImportoIniziale">false</s:param>
							</s:include>
						</fieldset>
						
						<!-- SIAC-8003 -->
						<s:hidden value="%{checkCapitoloCategoria()}" id="capitoloCat" ></s:hidden>
						
						<p>
							<!-- AGGIORNAMENTO -->
							<!--SIAC-6884 -->
							<s:if test="fromPage == 'MODIFY'">
								<s:hidden id="uidCapitoloHidden" value="%{uidCapitolo}"></s:hidden>
								<s:a cssClass="btn" action="aggiornaCapUscitaPrevisione.do"
								id="pulsanteRedirezioneIndietro">
								<s:param name="uidDaAggiornare" value="%{uidCapitolo}"/>
								<s:param name="daAggiornamento" value="true"/>
								indietro</s:a>
		
							</s:if>
							
							<s:if test="fromPage == 'INSERT'">
								<s:a cssClass="btn" action="aggiornaCapUscitaPrevisione.do"
								id="pulsanteRedirezioneIndietro">
								<s:param name="uidDaAggiornare" value="%{uidCapitolo}"/>
								<s:param name="daAggiornamento" value="true"/>
								indietro</s:a>
							
							</s:if>
						</p>
						
					</div>
					
									<!--SIAC 6881 -->
					<%-- Modale Inserimento Modifiche --%>
					<div id="componenteModel" class="modal hide fade" tabindex="-1" role="dialog"
						aria-labelledby="myEditModalLabel" aria-hidden="true">
						<div class="overlay-modale">
							<div class="row-fluid">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h3 id="titoloModaleInserisciModifica"></h3>
									
								</div>
								<!-- style="height:160px;" -->
								<div class="modal-body" >
									<div class="alert alert-error hide" id="ERRORI_modaleInsertStanziamenti">
										<button type="button" class="close" data-hide="alert">&times;</button>
										<strong>Attenzione!!</strong><br>
										<font id="descrizioneErrore"></font>
										<ul>
										</ul>
									</div>

									<!-- SIAC 6881 - Modale nuova -->
									<div id="bodyModal">
										<s:select list="listaTipoComponentiPerNuovaComponente" cssClass="" required="true" 
										id="dropdown-select-com" name="componenteSelezionata" headerKey="0" headerValue="-- Seleziona --" listKey="uid" listValue="%{descrizione}" />
										<br/>
										<div id="divTabellaInserimentoImportiNuovaComponente">
											<table id="tabellaInserimentoImportiNuovaComponente" class="table table-condensed table-bordered">
												<thead>
													<tr>
														<th>&nbsp;</th>
														<th class="text-right">${annoEsercizioInt - 1}</th>
														<th class="text-right">Residui ${annoEsercizioInt + 0}</th>
														<th class="text-right">${annoEsercizioInt + 0}</th>
														<th class="text-right">${annoEsercizioInt + 1}</th>
														<th class="text-right">${annoEsercizioInt + 2}</th>
														<th class="text-right">>${annoEsercizioInt + 2}</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
										
										</div>
										
										<!-- SIAC 8859 - Modale nuova  BEGIN-->
										<div class="bodyModal alert alert-warning" id="CONFERMA_modaleEditComponenti">
											<div class="overlay-modale">
												<div>
													<button type="button" class="close" data-hide="alert">&times;</button>
													<p>
														<strong>ATTENZIONE!!!</strong>
													</p>
													<p>Procedendo, gli importi indicati a livello di componente verranno comunque gi&agrave; salvati sul capitolo di previsione (anche a fronte di successivo annulla o indietro sulla videata del capitolo).<br>
													   Per ritornare alla versione precedente andr&agrave; fatta una nuova modifica. Si desidera procedere?</p>
												</div>
												<div class="modal-footer">
													<button class="btn" data-dismiss="modal" aria-hidden="true" id="EDIT_confermaCompIndietro">no, indietro</button>
													<button type="button" class="btn btn-primary" id="EDIT_confermaCompOperazione">s&iacute;,
														prosegui</button>
												</div>
											</div>
										</div>
										<!-- SIAC 8859 - Modale nuova  END-->
									
									</div>
									
									
									<!-- SIAC 6881 - Modale nuova  END-->

									
								</div>								<div class="modal-footer">
									<button type="button" class="btn" data-dismiss="modal"
										aria-hidden="true" id="chiudiModaleComp">chiudi</button>
									<button type="button" class="btn btn-primary" id="INSERT_modifica">conferma</button>
								</div>
							</div>
						</div>
					</div>


					<%-- Modale Inserimento Modifiche --%>
					<div id="editStanziamentiComponente" class="modal hide fade" tabindex="-1" role="dialog"
						aria-labelledby="myEditModalLabel" aria-hidden="true">
						<div class="overlay-modale">
							<div class="row-fluid">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h3 id="titoloModaleInserisciModifica"></h3>
									
								</div>
								<!-- style="height:160px;" -->
								<div class="modal-body" >
									<div class="alert alert-error" id="ERRORI_modaleEditStanziamenti">
										<button type="button" class="close" data-hide="alert">&times;</button>
										<strong>Attenzione!!</strong><br>
										<font id="descrizioneErrore"></font>
										<ul>
										</ul>
									</div>

									<!-- SIAC 6881 - Modale nuova -->
									<div id="bodyModal">
										
										<table id="tabellaModificaImporti" class="table table-condensed table-bordered">
											<thead>
												<tr>
													<th>Componente</th>
													<th>&nbsp;</th>
													<th class="text-right">${annoEsercizioInt - 1}</th>
													<th class="text-right">Residui ${annoEsercizioInt + 0}</th>
													<th class="text-right">${annoEsercizioInt + 0}</th>
													<th class="text-right">${annoEsercizioInt + 1}</th>
													<th class="text-right">${annoEsercizioInt + 2}</th>
													<th class="text-right">>${annoEsercizioInt + 2}</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
										
									
									</div>
									
									
									<!-- SIAC 6881 - Modale nuova  END-->

									
									<!-- SIAC 8859 - Modale nuova  BEGIN-->
									<div class="bodyModal alert alert-warning" id="CONFERMA_modaleEditStanziamenti">
										<div class="overlay-modale">
											<div>
												<button type="button" class="close" data-hide="alert">&times;</button>
												<p>
													<strong>ATTENZIONE!!!</strong>
												</p>
												<p>Procedendo, gli importi indicati a livello di componente verranno comunque gi&agrave; salvati sul capitolo di previsione (anche a fronte di successivo annulla o indietro sulla videata del capitolo).<br>
												   Per ritornare alla versione precedente andr&agrave; fatta una nuova modifica. Si desidera procedere?</p>
											</div>
											<div class="modal-footer">
												<button class="btn" data-dismiss="modal" aria-hidden="true" id="EDIT_confermaIndietro">no, indietro</button>
												<button type="button" class="btn btn-primary" id="EDIT_confermaOperazione">s&iacute;,
													prosegui</button>
											</div>
										</div>
									</div>
									<!-- SIAC 8859 - Modale nuova  END-->
									
								</div>
								<div class="modal-footer">
									<button type="button" class="btn" data-dismiss="modal"
										aria-hidden="true" id="chiudiModale">chiudi</button>
									<button type="button" class="btn btn-primary button-conferma-importi hide hide-modal-element-importo" id="INSERT_modificaImporti">inserisci modifiche</button>
									<button type="button" class="btn btn-primary button-conferma-componenti hide hide-modal-element-componenti" id="INSERT_modificaComponenti">inserisci modifiche</button>
								</div>
							</div>
						</div>
					</div>
										
					
					<%-- Modale eliminazione --%>
					<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog"
						aria-labelledby="msgEliminaLabel" aria-hidden="true">
						<div class="overlay-modale">
							<div class="modal-body">
								<div class="alert alert-error  alert-persistent">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<p>
										<strong>Attenzione!!!</strong>
									</p>
									<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								<button type="button" class="btn btn-primary" id="EDIT_elimina">s&iacute;,
									prosegui</button>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/tabellaComponenteImportiCapitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaPrevisione/gestioneComponenteImportiCapitoloNelCapitolo.js"></script>
</body>
</html>