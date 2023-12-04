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
				<s:include value="/jsp/include/messaggi.jsp" />
					<s:form action="effettuaRicercaTipoDocumento" novalidate="novalidate" id="formRicercaTipoDocumento">
						<h3>Ricerca Tipo Documento FEL - Contabilia</h3>
						<div class="step-content">
						<div class="step-pane active">
						
							 
									<fieldset id="formRicercaTipoDocumento" class="form-horizontal">
										<br>
								 
		
										<div class="control-group">
											<label class="control-label" for="codiceTipoDocumento">Codice FEL</label>
											<div class="controls">
												<s:textfield id="codiceTipoDocumento" cssClass="span6" name="tipoDocFel.codice"   maxlength="4"/>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label" for="descrizioneTipoDocumento">Descrizione</label>
											<div class="controls">
												<s:textfield id="descrizioneTipoDocumento" cssClass="span6" name="tipoDocFel.descrizione" maxlength="200" />
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
							<p class="margin-large">
								<s:include value="/jsp/include/indietro.jsp" />
								<s:reset cssClass="btn btn-link" value="annulla" />
								<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
 
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca.js"></script>

</body>
</html>