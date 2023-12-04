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
					<h3>Aggiorna Tipo Documento FEL - Contabilia</h3>
					<div class="step-content">
					 
							<%-- Dati Componente  --%>
							<h4> Dati </h4>
							<s:if test="tipoDocumento.collegatoAFatture">
								<div id="warning_tipoDoc" class="alert alert-warning ">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<strong>Attenzione!!</strong><br>
									<ul><li>Tipo documento già associato a fatture. E' possibile aggiornare la descrizione,Tipo documento Contabilia Entrata,Tipo documento Contabilia Spesa </li>
									</ul>
								</div>
							</s:if>
							<s:form action="aggiornaTipoDocumentoAction_aggiornamentoTD" id="formAggiornamentoTipoDocumento" novalidate="novalidate"> 
								<s:hidden id="HIDDEN_forzaTipoDocumento" name="forzaTipoDocumento" />
								<s:if test="%{richiediConfermaUtente}">
										<s:hidden id="HIDDEN_richiediConfermaUtente" data-messaggio-conferma = "%{messaggioRichiestaConfermaProsecuzione}" />
								</s:if>
				 
								<s:hidden name="tipoDocFel.codice"/>
								<s:hidden name="tipoDocFel.uid"/>
					 
								<s:hidden name="tipoDocFel.collegatoAFatture" />

								<div class="fieldset-body">
									<fieldset class="form-horizontal">
									
									<div class="control-group">
											<label for="codiceTipoDocumento" class="control-label">Codice FEL*</label>
											<div class="controls">
												 
												<s:textfield id="codiceTipoDocumento" cssClass="span6" name="tipoDocFel.codice" required="required" maxlength="4"  disabled="true"/>
											</div>
										</div>
										
										<div class="control-group">
											<label for="descrizioneTipoDocumento" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textfield id="descrizioneTipoDocumento" cssClass="span6" name="tipoDocFel.descrizione" required="required" maxlength="200" required="required"/>
											</div>
										</div>
									
										<div class="control-group">
											<label for="tipoDocContabiliaSpesa" class="control-label">Tipo documento Contabilia Spesa</label>
											<div class="controls">
										 	 <s:select list="listaTipoDocContabiliaSpesa" cssClass="span6" id="tipoDocContabiliaSpesa" name="tipoDocFel.tipoDocContabiliaSpesa.uid" headerKey="0" headerValue=""  
												listKey="uid" listValue="%{codice + ' - ' + descrizione}"  />
											</div>
										</div>
	
										<div class="control-group">
											<label for="tipoDocContabiliaEntrata" class="control-label">Tipo documento Contabilia Entrata </label>
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
									<s:a action="aggiornaTipoDocumento.do" class="btn btn-secondary">
											<s:param name="uidTipoDocumento" value="%{tipoDocFel.codice}"/>
									annulla</s:a>
									<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
								</p>
							</s:form>
						</div>
					</div>
				</div>
			</div>
	 

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaTipoDocumento/aggiorna.js"></script>
 
</body>
</html>