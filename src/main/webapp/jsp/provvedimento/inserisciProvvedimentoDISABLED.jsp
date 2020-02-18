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
						<s:form action="salvaInserimentoProvvedimento" id="editProvvedimento" novalidate="novalidate">
							<div class="fieldset-body">
								<fieldset class="form-horizontal">
									<br>
									<div class="control-group">
										<label class="control-label" for="anno">Anno *</label>
										<div class="controls">
											<s:textfield type="text" name="attoAmministrativo.anno" id="anno" cssClass="lbTextSmall span2" required="required" maxlength="4" placeholder="Anno" disabled="true" />
											<span class="al">
												<label class="radio inline" for="numero">Numero *</label>
											</span>
											<s:textfield type="text" name="attoAmministrativo.numero" id="numero" cssClass="lbTextSmall span2" required="required" maxlength="6" placeholder="Numero" disabled="true" />
											<span class="al">
												<label class="radio inline" for="tipoAtto">Tipo Atto *</label>
											</span>
											<s:select list="tipiAtti" listKey="uid" listValue="descrizione" name="attoAmministrativo.tipoAtto.uid" id="tipoAtto" cssClass="lbTextSmall span3" disabled="true" />
										</div>
									</div>
									<div class="control-group">
										<label for="strutturaAmministrativoContabile" class="control-label">Struttura Amministrativa Responsabile</label>
										<div class="controls">
											<span id="SPAN_StrutturaAmministrativoContabile">
												<s:if test='%{attoAmministrativo.strutturaAmmContabile == null || attoAmministrativo.strutturaAmmContabile.descrizione eq ""}'>
													Nessuna Struttura Amministrativa Responsabile selezionata
												</s:if><s:else>
													<s:property value="attoAmministrativo.strutturaAmmContabile.descrizione"/>
												</s:else>
											</span>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="oggetto">Oggetto</label>
										<div class="controls">
											<s:textfield id="oggetto" cssClass="lbTextSmall span9" name="attoAmministrativo.oggetto" maxlength="500" disabled="true" />
										</div>
									</div>
									<div class="control-group">
										<label for="statoOperativo" class="control-label">Stato operativo</label>
										<div class="controls">
											<s:select list="statiOperativi" name="attoAmministrativo.statoOperativo" id="statoOperativo" cssClass="lbTextSmall span10" disabled="true" headerKey="" headerValue=""/>
										</div>
									</div>
									<div class="control-group">
										<label for="note" class="control-label">Note</label>
										<div class="controls">
											<s:textarea rows="5" cols="15" id="note" name="attoAmministrativo.note" cssClass="span10" maxlength="500" disabled="true" />
										</div>
									</div>
									<div class="control-group">
									<label class="control-label" for="parereRegolaritaContabile">Parere di Regolarit&agrave; Contabile *</label>
									<div class="controls">
										<s:checkbox id="parereRegolaritaContabile" name="attoAmministrativo.parereRegolaritaContabile" disabled="true" />
									</div>
								</div>
								</fieldset>
							</div>
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
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
</body>
</html>
