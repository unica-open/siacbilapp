<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
					<h3>Inserisci Tipo Documento FEL - Contabilia</h3>
					<div class="step-content">
						<div class="step-pane active">
							<h4>Dati</h4>
							<s:form action="inserisciTipoDocumentoAction_inserimentoTD" id="formInserimentoTipoDocumento" novalidate="novalidate">
								<s:hidden id="HIDDEN_forzaTipoDocumento" name="forzaTipoDocumento" />
								<s:if test="%{richiediConfermaUtente}">
										<s:hidden id="HIDDEN_richiediConfermaUtente" data-messaggio-conferma = "%{messaggioRichiestaConfermaProsecuzione}" />
								</s:if>
							 
									<fieldset class="form-horizontal">
										
										
										<div class="control-group">
											<label for="codiceTipoDocumento" class="control-label">Codice FEL *</label>
											<div class="controls">
												<s:textfield id="codiceTipoDocumento" cssClass="span6" name="tipoDocFel.codice" required="required" maxlength="4"/>
											</div>
										</div>
										
										<div class="control-group">
											<label for="descrizioneTipoDocumento" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textfield id="descrizioneTipoDocumento" cssClass="span6" name="tipoDocFel.descrizione" required="required" maxlength="200"/>
											</div>
										</div>
										
										
										<div class="control-group">
											<label for=tipoDocContabiliaSpesa class="control-label">Tipo documento Contabilia Spesa</label>
											<div class="controls">
											 <s:select list="listaTipoDocContabiliaSpesa" cssClass="span6" id="tipoDocContabiliaSpesa" name="tipoDocFel.tipoDocContabiliaSpesa.uid" headerKey="0" headerValue=""  
												listKey="uid" listValue="%{codice + ' - ' + descrizione}"  />
													
											</div>
										</div>
										
										<div class="control-group">	 
											<label for="tipoDocContabiliaEntrata" class="control-label">Tipo documento Contabilia Entrata</label>
											<div class="controls">
											 <s:select list="listaTipoDocContabiliaEntrata" cssClass="span6" id="tipoDocContabiliaEntrata" name="tipoDocFel.tipoDocContabiliaEntrata.uid" headerKey="0" headerValue=""  
												listKey="uid" listValue="%{codice + ' - ' + descrizione}"   />
											 
											</div>
										</div>
	 
	 
									</fieldset>
								</div>
								</div>
								<p class="margin-medium">
									<s:include value="/jsp/include/indietro.jsp" />
									<s:a action="annullaInserisciTipoDocumento" class="btn btn-secondary">annulla</s:a>
									<s:submit cssClass="btn btn-primary pull-right" value="salva" />
								</p>
							</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaTipoDocumento/tipoDocumento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaTipoDocumento/inserisci.js"></script>
</body>
</html>