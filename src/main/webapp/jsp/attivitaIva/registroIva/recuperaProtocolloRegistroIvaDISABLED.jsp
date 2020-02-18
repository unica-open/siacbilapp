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
					<s:form cssClass="form-horizontal" action="recuperaProtocolloRegistroIva_recuperaProtocollo" id="formAggiornamentoRegistroIva" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="uidRegistroIva" data-maintain="" />
						<h3>Recupera protocollo registro iva</h3>
						<div class="step-content">
							<div class="step-pane active">
								<h4>Dati principali</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="gruppoAttivitaIva" class="control-label">Gruppo attivit&agrave; iva *</label>
										<div class="controls">
											<s:select list="listaGruppoAttivitaIva" id="gruppoAttivitaIva" name="gruppoAttivitaIva.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" data-maintain="" />
											<s:hidden name="gruppoAttivitaIva.uid" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="tipoRegistroIva" class="control-label">Tipo registro iva *</label>
										<div class="controls">
											<s:select list="listaTipoRegistroIva" id="tipoRegistroIva" name="tipoRegistroIva" cssClass="span6" required="true" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" disabled="true" data-maintain="" />
											<s:hidden name="tipoRegistroIva" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="codiceRegistroIva" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codiceRegistroIva" name="registroIva.codice" cssClass="lbTextSmall span2" disabled="true" data-maintain="" />
											<s:hidden name="registroIva.codice" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneRegistroIva" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textarea id="descrizioneRegistroIva" name="registroIva.descrizione" cssClass="input-medium span9" cols="15" rows="1" required="true" disabled="true"></s:textarea>
										</div>
									</div>
									<div class="control-group" >
										<label for="numeroProtocolloDefinitivoSubdocumentoIva" class="control-label"><abbr title="Numero Protocollo Definitivo">Num. Prot. Def.</abbr> da recuperare</label>
										<div class="controls">
											<s:textfield id="numeroProtocolloDefinitivoSubdocumentoIva" name="numeroProtocolloDefinitivo" cssClass="lbTextSmall span2 soloNumeri" value="%{numeroProtocolloDefinitivo}" disabled="true" data-maintain="" />
										</div>
									</div>
									<s:if test="%{protocolloProvvisorioModificabile}">
										<div class="control-group" >
											<label for="numeroProtocolloProvvisorioSubdocumentoIva" class="control-label"><abbr title="Numero Protocollo Provvisorio">Num. Prot. Provv.</abbr> da recuperare</label>
											<div class="controls">
												<s:textfield id="numeroProtocolloProvvisorioSubdocumentoIva" name="numeroProtocolloProvvisorio" cssClass="lbTextSmall span2 soloNumeri" value="%{numeroProtocolloProvvisorio}" disabled="true" data-maintain="" />											
											</div>
										</div>
									</s:if>
								</fieldset>
							</div>
						</div>
						
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-secondary reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="recupera protocollo" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>