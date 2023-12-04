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
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="ricercaFatturaElettronica_effettuaRicerca" novalidate="novalidate" id="formFatturaElettronica">
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Ricerca FEL</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<p>
									<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
								</p>
								<h4 class="step-pane">Dati </h4>
								<div class="control-group">
									<label class="control-label" for="tipoDocumentoFELFatturaFEL">Tipo documento FEL</label>
									<div class="controls">
									<%-- SIAC-7557 Estraggo i valori dal db 
										<s:select list="listaTipoDocumentoFELDB" id="tipoDocumentoFELFatturaFEL" name="fatturaFEL.tipoDocFEL" cssClass="span6"
											headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />--%>
											
									 <s:select list="listaTipoDocumentoFELDB" cssClass="span6" id="tipoDocumentoFELFatturaFEL" name="fatturaFEL.tipoDocFEL.codice"  headerValue=""   headerKey="" 
												listKey="codice" listValue="%{codice + ' - ' + descrizione}"  />		
											
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceFiscale">Codice fiscale</label>
									<div class="controls">
										<s:textfield id="codiceFiscale" name="codiceFiscale" cssClass="span4" maxlength="16" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="partitaIva">Partita Iva</label>
									<div class="controls">
										<s:textfield id="partitaIva" name="partitaIva" cssClass="span4" maxlength="11" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroFatturaFEL">Numero documento</label>
									<div class="controls">
										<s:textfield id="numeroFatturaFEL" name="fatturaFEL.numero" cssClass="span2" maxlength="20" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceDestinatarioFatturaFEL">Ufficio destinatario</label>
									<div class="controls">
										<s:select id="codiceDestinatarioFatturaFEL" list="listaCodiceUfficioDestinatarioPCC" name="fatturaFEL.codiceDestinatario" cssClass="span6" headerKey="" headerValue="" listKey="codice" listValue="descrizione" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codicePCC">Codice PCC</label>
									<div class="controls">
										<%--s:select id="codicePCC" list="listaCodicePCC" name="codicePCC.codice" cssClass="span6" headerKey="" headerValue="" listKey="codice" listValue="codice" /--%>
										<s:textfield name="codicePCC.codice" cssClass="span2" disabled="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data fattura</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataFatturaDa">Da</label>
										</span>
										<s:textfield id="dataFatturaDa" name="dataFatturaDa" cssClass="span2 datepicker" maxlength="10" />
										<span class="al">
											<label class="radio inline" for="dataFatturaA">A</label>
										</span>
										<s:textfield id="dataFatturaA" name="dataFatturaA" cssClass="span2 datepicker" maxlength="10" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="statoFatturaFatturaFEL">Stato acquisizione documento</label>
									<div class="controls">
										<s:select list="listaStatoAcquisizioneFEL" name="fatturaFEL.statoAcquisizioneFEL" cssClass="span6" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset value="annulla" cssClass="btn btn-secondary" />
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>