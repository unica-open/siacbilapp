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

	<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
					<h3>${titolo}</h3>
					<div class="step-content">
						<s:form action="salvaInserisciProvvedimento" id="editProvvedimento" novalidate="novalidate">
							<div class="fieldset-body">
								<fieldset class="form-horizontal">
									<br>
									<div class="control-group">
										<label class="control-label" for="anno">Anno *</label>
										<div class="controls">
											<s:textfield type="text" name="attoAmministrativo.anno" id="anno" cssClass="lbTextSmall span2 soloNumeri" required="required" maxlength="4" placeholder="Anno" />
											<span class="al">
												<label class="radio inline" for="numero">Numero *</label>
											</span>
											<s:textfield type="text" name="attoAmministrativo.numero" id="numero" cssClass="lbTextSmall span2 soloNumeri" required="required" maxlength="7" placeholder="Numero" />
											<span class="al">
												<label class="radio inline" for="tipoAtto">Tipo Atto *</label>
											</span>
											<select name="attoAmministrativo.tipoAtto.uid" required id="tipoAtti" class="lbTextSmall span3">
												<option value=""></option>
												<s:iterator value="tipiAtti" var="ta">
													<option value="<s:property value="#ta.uid" />"<s:if test="#ta.progressivoAutomatico"> data-progressivo-automatico</s:if>><s:property value="#ta.descrizione" /></option>
												</s:iterator>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label for="strutturaAmministrativoContabile" class="control-label">
											Struttura Amministrativa Responsabile
										</label>
										<div class="controls">
											<s:hidden type="hidden" id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid"/>
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileStringa" name="attoAmministrativo.strutturaAmmContabile.descrizione" />
											<a href="#struttAmm" class="btn" id="bottoneSAC" data-toggle="modal">
												Seleziona la Struttura amministrativa &nbsp;
												<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
											</a>
											<div id="struttAmm" class="modal hide fade" tabindex="-1">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal">&times;</button>
													<h3 id="myModalLabel2">
														Struttura Amministrativa Responsabile
													</h3>
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
												<s:if test="%{attoAmministrativo.strutturaAmmContabile != null}">
													<s:property value="attoAmministrativo.strutturaAmmContabile.descrizione"/>
												</s:if><s:else>
													Nessuna Struttura Amministrativa Responsabile selezionata
												</s:else>
											</span>
										</div>
									</div>
		
									<div class="control-group">
										<label class="control-label" for="oggetto">Oggetto</label>
										<div class="controls">
											<s:textfield id="oggetto" cssClass="lbTextSmall span9" name="attoAmministrativo.oggetto" maxlength="500" />
										</div>
									</div>
									<div class="control-group">
										<label for="statoOperativo" class="control-label">Stato operativo</label>
										<div class="controls">
											<s:select list="statiOperativi" name="attoAmministrativo.statoOperativo" id="statoOperativo" cssClass="lbTextSmall span10" headerKey="" headerValue="" listKey="descrizione" listValue="descrizione" />
										</div>
									</div>
									<div class="control-group">
										<label for="note" class="control-label">Note</label>
										<div class="controls">
											<s:textarea rows="5" cols="15" id="note" name="attoAmministrativo.note" cssClass="span10" maxlength="500" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="parereRegolaritaContabile">Parere di Regolarit&agrave; Contabile *</label>
										<div class="controls">
											<input type="checkbox" id="parereRegolaritaContabile" disabled />
										</div>
									</div>
								</fieldset>
							</div>
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<input type="reset" value="annulla" class="btn btn-link" />
								<input type="submit" value="salva" class="btn btn-primary pull-right" >
							</p>
						</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/inserisci.js"></script>
</body>
</html>
