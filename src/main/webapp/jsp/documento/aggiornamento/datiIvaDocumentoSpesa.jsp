<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="divAnagraficaIva" class="step-pane active form-horizontal">
	<div class="accordion">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a href="#collapseDatiAnagraficaIva" data-parent="#divAnagraficaIva" data-toggle="collapse" class="accordion-toggle collapsed">
					Dati principali<span class="icon">&nbsp;</span>
				</a>
			</div>
			<div class="accordion-body collapse" id="collapseDatiAnagraficaIva">
				<div class="accordion-inner">
					<fieldset class="form-horizontal">
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Dati anagrafica</p>
								<ul class="htmlelt">
									<li>
										<dfn>Data emissione</dfn>
										<dl><s:property value="documento.dataEmissione" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Data scadenza</dfn>
										<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Codice fiscale</dfn>
										<dl><s:property value="soggetto.codiceFiscale"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Partita iva</dfn>
										<dl><s:property value="soggetto.partitaIva"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="documento.descrizione" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Sede secondaria</dfn>
										<dl><s:property value="sedeSecondariaSoggetto.denominazione" />&nbsp;</dl>
									</li>
								</ul>
							</div>
							<div class="boxOrInRight">
								<p>Importi</p>
								<ul class="htmlelt">
									<li>
										<dfn>Importo totale documento</dfn>
										<dl><s:property value="documento.importo" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Importo non rilevante iva</dfn>
										<dl><s:property value="importoNonRilevanteIva" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Importo rilevante iva</dfn>
										<dl><s:property value="importoRilevanteIva" />&nbsp;</dl>
									</li>
								</ul>
							</div>
						</div>
					</fieldset>
				</div>
			</div>
		</div>
	</div>
	
	<h4 class="step-pane">Dati iva</h4>
	<div class="control-group">
		<span class="control-label">Inserimento dati iva</span>
		<div class="controls">
			<label class="radio inline">
				<input type="radio" value="Documento" name="tipoInserimentoDatiIva" <s:if test='%{"Documento".equals(tipoInserimentoDatiIva)}'>checked</s:if>> Su intero documento
			</label>
			<label class="radio inline">
				<input type="radio" value="Quota" name="tipoInserimentoDatiIva" <s:if test='%{"Quota".equals(tipoInserimentoDatiIva)}'>checked</s:if>> Sui singoli dettagli del documento (quote)
			</label>
		</div>
	</div>
	<s:hidden name="legameConIvaPresente" id="HIDDEN_legameConIvaPresente" />
	<s:hidden name="documentoIvaLegatoDocumentoPresente" id="HIDDEN_documentoIvaLegatoDocumentoPresente" />
	<div id="inserimentoSubdocumentoIvaDocumento" class="<s:if test='%{!"Documento".equals(tipoInserimentoDatiIva)}'>hide</s:if>">
		<div class="control-group">
			<label for="annoEsercizioSubdocumentoIva" class="control-label">Registrazione iva</label>
			<div class="controls">
				<s:textfield id="annoEsercizioSubdocumentoIva" name="subdocumentoIva.annoEsercizio" cssClass="lbTextSmall span1" disabled="true" />
				<span class="alRight">
					<label for="DataReg" class="radio inline">/</label>
				</span>
				<s:textfield id="progressivoIVASubdocumentoIva" name="subdocumentoIva.progressivoIVA" cssClass="lbTextSmall span2" disabled="true" />
			</div>
		</div>
		<div class="control-group">
		<label for="ImpTotMovIva" class="control-label">Importo totale movimenti iva</label>
			<div class="controls">
				<s:textfield id="progressivoIVASubdocumentoIva" name="importoTotaleMovimentiIva" cssClass="lbTextSmall span2" disabled="true" value="%{subdocumentoIva.calcolaTotaleMovimentiIva()}" />
			</div>
		</div>
		<p>
			<span class="pull-right">
				<s:if test="documentoIvaLegatoDocumentoPresente">
					<a class="btn btn-primary" href="aggiornamentoDocumentoSpesa_redirezioneAggiornamentoDocumentoIvaSpesa_documento.do?uidSubdocumentoIva=<s:property value="subdocumentoIva.uid" />">aggiorna documento iva di spesa</a>
				</s:if><s:else>
					<a class="btn btn-primary" href="aggiornamentoDocumentoSpesa_redirezioneInserimentoDocumentoIvaSpesa_documento.do">inserisci documento iva di spesa</a>
				</s:else>
			</span>
		</p>
	</div>
	
	<div id="inserimentoSubdocumentoIvaQuota" class="<s:if test='%{!"Quota".equals(tipoInserimentoDatiIva)}'>hide</s:if>">
		<h4>Elenco quote rilevanti iva&nbsp;<i id="SPINNER_elencoQuoteRilevantiIva" class="icon-spin icon-refresh spinner"></i></h4>
		<table class="table table-hover tab_left" id="tabellaQuoteRilevantiIva">
			<thead>
				<tr>
					<th class="span1" scope="col">Numero</th>
					<th scope="col">Impegno</th>
					<th scope="col">Capitolo</th>
					<th scope="col">Attivit&agrave; iva</th>
					<th scope="col">Registrazione iva</th>
					<th class="tab_Right" scope="col">Importo quota</th>
					<th class="tab_Right" scope="col">Importo totale movimenti iva</th>
					<th class="tab_Right span1" scope="col">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
			</tfoot>
		</table>
	</div>
	<div class="clear"></div>
	<br />
	<div class="Border_line"></div>
</div>
<p class="margin-medium">
	<s:include value="/jsp/include/indietro.jsp" />
</p>