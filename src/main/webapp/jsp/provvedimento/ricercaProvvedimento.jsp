<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

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
					<s:form action="effettuaRicercaConOperazioniProvvedimento" novalidate="novalidate" id="formRicercaConOperazioniProvvedimento">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Ricerca provvedimento</h3>
						<div class="step-content">
							<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
							<div class="fieldset-body">
								<fieldset id="formRicercaProvvedimento" class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="annoProvvedimento">Anno *</label>
										<div class="controls">
											<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.anno" maxlength="4" placeholder="Anno" required="required" />
											<span class="al">
												<label class="radio inline" for="numeroProvvedimento">Numero</label>
											</span>
											<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="6" placeholder="Numero" />
											<span class="al">
												<label class="radio inline" for="tipoAtto">Tipo Atto</label>
											</span>
											<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid" id="tipoAtto" cssClass="lbTextSmall span3"
													headerKey="" headerValue="" />
										</div>
									</div>
									<div class="control-group">
										<label for="bottoneSAC" class="control-label">Struttura Amministrativa Responsabile</label>
										<div class="controls">
											<a href="#struttAmm" role="button" class="btn" id="bottoneSAC" data-toggle="modal">
												Seleziona la Struttura amministrativa &nbsp;
												<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
											</a>
											<!-- Modal -->
											<div id="struttAmm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
													<h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
												</div>
												<div class="modal-body">
													<ul id="treeStruttAmm" class="ztree"></ul>
												</div>
												<div class="modal-footer">
													<button id="deselezionaStrutturaAmministrativaResponsabile" class="btn">Deseleziona</button>
													<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
												</div>
											</div>
											&nbsp;
											<span id="SPAN_StrutturaAmministrativoContabile">
												<s:if test='%{strutturaAmministrativoResponsabile != null && strutturaAmministrativoResponsabile neq ""}'>
													<s:property value="strutturaAmministrativoResponsabile"/>
												</s:if><s:else>
													Nessuna Struttura Amministrativa Responsabile selezionata
												</s:else>
											</span>
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="strutturaAmministrativoResponsabile" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="oggettoProvvedimento">Oggetto</label>
										<div class="controls">
											<s:textfield id="oggettoProvvedimento" cssClass="lbTextSmall span9" name="attoAmministrativo.oggetto" maxlength="500" placeholder="Oggetto" />
										</div>
									</div>
									<div class="control-group">
										<label for="statoOperativoProvvedimento" class="control-label">Stato operativo</label>
										<div class="controls">
											<s:select list="listaStatoOperativo" id="statoOperativoProvvedimento" cssClass="span10" name="statoOperativoAtti" headerKey="" headerValue="" />
										</div>
									</div>
									<%-- SIAC 6929 --%>
									<div class="control-group">
										<span class="control-label">Blocco Ragioneria</span>
											<div class="controls">
												<label class="radio-inline" style="display:initial; margin-right: 10px;">
													<s:radio id="bloccoRagioneria"  name="bloccoRagioneria" list="#{'TUTTI':'Tutti'}" /> Tutti
												</label>
												<label class="radio-inline" style="display:initial; margin-right: 10px;"> 
													<s:radio id="bloccoRagioneria" name="bloccoRagioneria" list="#{'SI':'Si'}" /> Si 
												</label>
												<label class="radio-inline" style="display:initial; margin-right: 10px;">
													<s:radio id="bloccoRagioneria"  name="bloccoRagioneria" list="#{'NO':'No'}" /> No
												</label>
											</div>	
									</div>
									<div class="control-group">
											<span class="control-label">Inserito Manualmente</span>
												<div class="controls">
													<label class="radio-inline" style="display:initial; margin-right: 10px;">
														<s:radio id="bloccoRagioneria"  name="inseritoManualmente" list="#{'TUTTI':'Tutti'}" /> Tutti
													</label>
													<label class="radio-inline" style="display:initial; margin-right: 10px;"> 
														<s:radio id="bloccoRagioneria" name="inseritoManualmente" list="#{'SI':'Si'}" /> Si 
													</label>
													<label class="radio-inline" style="display:initial; margin-right: 10px;">
														<s:radio id="bloccoRagioneria"  name="inseritoManualmente" list="#{'NO':'No'}" /> No
													</label>
												</div>	
											</div>
									<!-- SIAC 6929 -->
									<div class="control-group">
										<label for="noteProvvedimento" class="control-label">Note</label>
										<div class="controls">
											<s:textarea id="noteProvvedimento" cssClass="span10" rows="5" cols="15" name="attoAmministrativo.note" maxlength="500" />
										</div>
									</div>
								</fieldset>
	
							</div>
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<input type="reset" value="annulla" class="btn btn-link" />
								<input type="submit" value="cerca" class="btn btn-primary pull-right" >
							</p>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}provvedimento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca.js"></script>

</body>
</html>