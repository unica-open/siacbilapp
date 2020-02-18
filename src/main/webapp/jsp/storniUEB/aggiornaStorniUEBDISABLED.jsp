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
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3>Aggiorna storni UEB</h3>

				<div class="well">
					<dl class="dl-horizontal">
						<dt>Numero storno:</dt>
						<dd>&nbsp;<s:property value="numeroStorno"/></dd>
						<dt>UEB Sorgente:</dt>
						<dd>&nbsp;<s:property value="tipoCapitoloSorgente"/>&nbsp;-&nbsp;<s:property value="numeroCapitoloSorgente"/>&nbsp;/&nbsp;<s:property value="numeroArticoloSorgente"/>&nbsp;/&nbsp;<s:property value="numeroUEBSorgente"/></dd>
						<dt>Disponibilit&agrave; Sorgente:</dt>
						<dd>&nbsp;<s:property value="disponibilitaCapitoloSorgente"/></dd>
						<dt>UEB Destinazione:</dt>
						<dd>&nbsp;<s:property value="tipoCapitoloDestinazione"/>&nbsp;-&nbsp;<s:property value="numeroCapitoloDestinazione"/>&nbsp;/&nbsp;<s:property value="numeroArticoloDestinazione"/>&nbsp;/&nbsp;<s:property value="numeroUEBDestinazione"/></dd>
						<dt>Disponibilit&agrave; Destinazione:</dt>
						<dd>&nbsp;<s:property value="disponibilitaCapitoloDestinazione"/></dd>
						<dt>Provvedimento:</dt>
						<dd>&nbsp;<s:property value="annoProvvedimento"/>&nbsp;/&nbsp;<s:property value="numeroProvvedimento"/>
					</dl>
				</div>
				<table summary="riepilogo incarichi" class="table table-hover">

					<tr>
						<th>&nbsp;</th>
						<th><s:property value="%{annoEsercizioInt}"/> </th>
						<th><s:property value="%{annoEsercizioInt + 1}"/></th>
						<th><s:property value="%{annoEsercizioInt + 2}"/></th>
					</tr>
					<tr>
						<th>Competenza</th>
						<td>
							<s:textfield id="competenzaStorno0" cssClass="input-small" name="competenzaStorno0" disabled="true" />
							<label for="competenzaStorno0" class="nascosto">inserisci importo</label>
						</td>
						<td>
							<s:textfield id="competenzaStorno1" cssClass="input-small" name="competenzaStorno1" disabled="true" />
							<label for="competenzaStorno1" class="nascosto">inserisci importo</label>
						</td>
						<td>
							<s:textfield id="competenzaStorno2" cssClass="input-small" name="competenzaStorno2" disabled="true" />
							<label for="competenzaStorno2" class="nascosto">inserisci importo</label>
						</td>
					</tr>
					<tr>
						<th>Cassa</th>
						<td>
							<s:textfield id="cassaStorno0" cssClass="input-small" name="cassaStorno0" disabled="true" />
							<label for="imp4" class="nascosto">inserisci importo</label>
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
				</table>
				<div class="margin-large">
					<a class="btn pull-center" data-toggle="collapse" data-parent="#accordion4" href="#collapse4">
						<i class="icon-pencil icon-2x"></i>&nbsp;provvedimento: <s:property value="annoProvvedimento"/>&nbsp;/&nbsp;<s:property value="numeroProvvedimento"/>
					</a>
				</div>
				<div id="collapse4" class="collapse">
					<div class="collapse-inner">
						<fieldset class="form-horizontal" id="formRicercaProvvedimento">
							<div class="control-group">
								<label class="control-label" for="annoProvvedimento">Anno *</label>
								<div class="controls">
									<input type="text" class="lbTextSmall span1" disabled="disabled" id="annoProvvedimento" />
									<%--<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1" name="annoProvvedimento" disabled="true"/>--%>
									<span class="al">
										<label class="radio inline" for="numeroProvvedimento">Numero</label>
									</span>
									<input type="text" class="lbTextSmall span1" disabled="disabled" id="numeroProvvedimento" />
									<%--<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span1" name="numeroProvvedimento" disabled="true" />--%>
									<span class="al">
										<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
									</span>
									<select class="lbTextSmall span2" id="tipoAttoProvvedimento" disabled="disabled"></select>
									<%--<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="uidTipoAtto" id="tipoAttoProvvedimento" cssClass="lbTextSmall span2"
										headerKey="" headerValue="" disabled="true" />--%>
									<span class="al">
										<label class="radio inline" for="strutturaAmministrativoContabile">Struttura Amministrativa</label>
									</span>
									<a href="#" role="button" class="btn" data-toggle="modal" disabled="disabled">
										Seleziona la Struttura amministrativa&nbsp;
										<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
									</a>
								</div>
							</div>
							<%--<div class="control-group">
								<label class="control-label" for="strutturaAmministrativoContabile">Struttura Amministrativa</label>
								<div class="controls">
									<s:property value="strutturaAmministrativoContabile.codice"/>-<s:property value="strutturaAmministrativoContabile.descrizione"/>
								</div>
							</div>--%>
							<div class="control-group">
								<label class="control-label" for="oggettoProvvedimento">Oggetto</label>
								<div class="controls">
									<input type="text" id="oggettoProvvedimento" class="lbTextSmall span9" disabled="disabled" />
									<%--<s:textfield id="oggettoProvvedimento" cssClass="lbTextSmall span9" name="attoAmministrativo.oggetto" maxlength="500" disabled="true" />--%>
								</div>
							</div>
						</fieldset>
					</div>
				</div>

				<p class="margin-large">
					<s:include value="/jsp/include/indietro.jsp" />
				</p>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>