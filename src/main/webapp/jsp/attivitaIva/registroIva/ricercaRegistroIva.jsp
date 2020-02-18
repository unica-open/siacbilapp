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
					<s:form cssClass="form-horizontal" action="effettuaRicercaRegistroIva" id="formRicercaRegistroIva" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Ricerca registro iva</h3>
						<div class="step-content">
							<div class="step-pane active">
								<h4>Dati principali</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="gruppoAttivitaIva" class="control-label">Gruppo attivit&agrave; iva</label>
										<div class="controls">
											<s:select list="listaGruppoAttivitaIva" id="gruppoAttivitaIva" name="gruppoAttivitaIva.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									<div class="control-group">
										<label for="tipoRegistroIva" class="control-label">Tipo registro iva</label>
										<div class="controls">
											<s:select list="listaTipoRegistroIva" id="tipoRegistroIva" name="tipoRegistroIva" cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									<div class="control-group">
										<label for="codiceRegistroIva" class="control-label">Codice</label>
										<div class="controls">
											<s:textfield id="codiceRegistroIva" name="registroIva.codice" cssClass="lbTextSmall span2" placeholder="%{'codice'}" />
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneRegistroIva" class="control-label">Descrizione</label>
										<div class="controls">
											<s:textarea id="descrizioneRegistroIva" name="registroIva.descrizione" cssClass="input-medium span9" cols="15" rows="1"></s:textarea>
										</div>
									</div>
								</fieldset>
							</div>
						</div>
						
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-secondary reset">annulla</button>
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
</body>
</html>