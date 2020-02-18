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
							
							<div class="alert alert-warning hide" id="alertErrori">
								<button type="button" class="close" data-hide="alert">&times;</button>
								<strong>Attenzione!!</strong><br>
								<ul id="errori"></ul>
							</div>
	
							<div>
							<fieldset class="form-horizontal" id="fieldset_inserimentoVariazioneImporti">
									<div class="control-group">
										<button type="button" class="btn" id="button_inserisciModifica" style="margin-bottom:10px;"  >Nuova Riga Componente</button>
										<table class="table table-hover table-condensed table-bordered"
										id="tabellaStanziamentiTotaliComponenti">
										<tr>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th class="text-right">${annoEsercizioInt - 1}</th>
											<th class="text-right">Residui ${annoEsercizioInt + 0}</th>
											<th class="text-right">${annoEsercizioInt + 0}</th>
											<th class="text-right">${annoEsercizioInt + 1}</th>
											<th class="text-right">${annoEsercizioInt + 2}</th>
											<th class="text-right">>${annoEsercizioInt + 2}</th>
										</tr>
										<tr>
											<th rowspan="3" class="stanziamenti-titoli">
												<a id="competenzaTotale" href="#" class="disabled">Competenza</a>
											</th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Impegnato</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Disponibilit&agrave; Impegnare</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<th rowspan="2" class="stanziamenti-titoli">
												Residuo
											</th>
											<td class="text-center">Presunto</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Effettivo</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<th rowspan="2" class="stanziamenti-titoli">
												Cassa
											</th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr>
											<td class="text-center">Pagato</td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
									</table>
									
									
									</div>

								</fieldset>								
							</div>
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
									<div id="idBodyInsertModal"></div>
									<!-- SIAC 6881 - Modale nuova  END-->

									
								</div>
								<div class="modal-footer">
									<button type="button" class="btn" data-dismiss="modal"
										aria-hidden="true">chiudi</button>
									<button type="button" class="btn btn-primary" id="INSERT_modifica">conferma</button>
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
					<%-- /Modale eliminazione --%>
					
					
					
					
				
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}capitoloUscitaPrevisione/componenteImportiCapitolo.js"></script>
</body>
</html>