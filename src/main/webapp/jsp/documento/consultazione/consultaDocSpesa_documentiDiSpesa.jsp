<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="form-horizontal">
	<h4>
		Inserimento: <s:property value="documento.dataCreazioneDocumento"/> (<s:property value="documento.loginCreazione"/>) 
		<br>
		Stato: <s:property value="documento.statoOperativoDocumento.descrizione"/> dal <s:property value="documento.dataInizioValiditaStato"/>
	</h4>
	
	<div class="boxOrInLeft">
		<p>Documenti di spesa</p>
		<ul class="htmlelt">
			<li>
				<dfn>Tipo</dfn>
				<dl><s:property value="documento.tipoDocumento.codice"/>&nbsp;-&nbsp;<s:property value="documento.tipoDocumento.descrizione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Tipo documento siope</dfn>
				<dl><s:property value="tipoDocumentoSiope"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Tipo documento analogico siope</dfn>
				<dl><s:property value="tipoDocumentoSiopeAnalogico"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Identificativo lotto SDI</dfn>
				<dl><s:property value="documento.siopeIdentificativoLottoSdi"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Anno</dfn>
				<dl><s:property value="documento.anno"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Numero</dfn>
				<dl><s:property value="documento.numero"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data emissione</dfn>
				<dl><s:property value="documento.dataEmissione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data ricezione</dfn>
				<dl><s:property value="documento.dataRicezionePortale"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice</dfn>
				<dl><s:property value="documento.soggetto.codiceSoggetto"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Denominazione</dfn>
				<dl><s:property value="documento.soggetto.denominazione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice fiscale</dfn>
				<dl><s:property value="documento.soggetto.codiceFiscale"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Partita IVA</dfn>
				<dl><s:property value="documento.soggetto.partitaIva"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Identificativo fiscale FEL</dfn>
				<dl><s:property value="identificativoFiscaleFEL" />&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice fiscale FEL</dfn>
				<dl><s:property value="codiceFiscaleFEL" />&nbsp;</dl>
			</li>
			<li>
				<dfn><abbr title="Struttura Amministrativo Contabile">Strutt.Amm.Cont.</abbr></dfn>
				<dl><s:property value="stringaDescrizioneSAC" />&nbsp;</dl>
			</li>
			<li>
				<dfn>Beneficiario multiplo</dfn>
				<dl>
					<s:if test="%{documento.flagBeneficiarioMultiplo}">S&igrave;</s:if><s:else>No</s:else>
				</dl>
			</li>
			<li>
				<dfn>Collegato a Cassa Economale</dfn>
				<dl>
					<s:if test="%{documento.collegatoCEC}">S&igrave;</s:if><s:else>No</s:else>
				</dl>
			</li>
			<%--  LOTTO M - Documenti Bozze - --%>
			<%--
			<li>
				<dfn>Attivazione registrazioni contabili</dfn>
				<dl>
					<s:if test="%{indicatoreContabilizzaGENPCC}">S&igrave;</s:if><s:else>No</s:else>
				</dl>
			</li>
			--%>
			<li>
				<dfn>Numero registro</dfn>
				<dl><s:property value="documento.registroUnico.numero"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data registro</dfn>
				<dl><s:property value="documento.registroUnico.dateRegistro"/>&nbsp;</dl>
			</li>
		</ul>
	</div>
	<div class="boxOrInRight">
		<p>Altri dati</p>
		<ul class="htmlelt">
			<li>
				<dfn>Importi</dfn>
				<dl>
					<span class="datiIns">&nbsp;<s:property value="documento.importo"/></span>&nbsp;&nbsp;
					<b>Arrotondamento</b><span class="datiIns">&nbsp;<s:property value="documento.arrotondamento"/></span>&nbsp;&nbsp;
					<b>Imponibile</b><span class="datiIns">&nbsp;<s:property value="netto"/></span>
				</dl>
			</li>
			<li>
				<dfn>Descrizione</dfn>
				<dl><s:property value="documento.descrizione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Termine di pagamento</dfn>
				<dl><s:property value="documento.terminePagamento"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data scadenza</dfn>
				<dl><s:property value="documento.dataScadenza"/>&nbsp;</dl>
			</li>
			<%-- LOTTO M - Documenti Bozze - --%>
			<%-- 
			<li>
				<dfn>Dati protocollo</dfn> 
				<dl>
					<s:if test='%{registroProtocollo != null && annoProtocollo != null}'>
						<b>Registro </b> <span class="datiIns">&nbsp;<s:property value="registroProtocollo"/></span>&nbsp;&nbsp;
						<b>Anno</b><span class="datiIns">&nbsp;<s:property value="annoProtocollo"/></span>
					</s:if>&nbsp;
				</dl>
			</li>
			--%>
			<li>
				<dfn>Dati repertorio/protocollo</dfn>
				<dl>
					<s:if test='%{documento.registroRepertorio != null }'>
						<b>Registro </b> <span class="datiIns">&nbsp;<s:property value="documento.registroRepertorio"/></span>&nbsp;&nbsp;
					</s:if>&nbsp;
					<s:if test='%{documento.annoRepertorio != null }'>
						<b>Anno</b><span class="datiIns">&nbsp;<s:property value="documento.annoRepertorio"/></span>
					</s:if>&nbsp;
					<s:if test='%{documento.numeroRepertorio != null && documento.dataRepertorio != null}'>
						<b>Numero </b> <span class="datiIns">&nbsp;<s:property value="documento.numeroRepertorio"/></span>&nbsp;&nbsp;
						<b>Data</b><span class="datiIns">&nbsp;<s:property value="documento.dataRepertorio"/></span>
					</s:if>&nbsp;
				</dl>
			</li>
			<!-- SIAC-6677 -->
			<!-- SIAC-6840 -->
 			<li>
 				<dfn>Codice Avviso Pago PA</dfn>
 				<dl><s:property value="documento.codAvvisoPagoPA"/>&nbsp;</dl>
 			</li>
			<!-- SIAC-6840 -->
			<li>
				<dfn>Soggetto pignorato</dfn>
				<dl><s:property value="documento.codiceFiscalePignorato"/>&nbsp;</dl>
			</li>		
			<li>
				<dfn>Codice bollo</dfn>
				<dl><s:property value="documento.codiceBollo.codice"/>&nbsp;-&nbsp;<s:property value="documento.codiceBollo.descrizione"/></dl>
			</li>
			<li>
				<dfn>Tipo impresa</dfn>
				<dl>
					<s:if test="%{documento.tipoImpresa != null}">
						<s:property value="documento.tipoImpresa.codice"/>&nbsp;-&nbsp;<s:property value="documento.tipoImpresa.descrizione"/>
					</s:if>&nbsp;
				</dl>
			</li>
			<%-- LOTTO M - Documenti Bozze - --%>
			<%--
			<li>
				<dfn>Ordini associati</dfn>
				<dl><s:property value="elencoOrdiniAssociati"/>&nbsp;</dl>
			</li>
			--%>
			<li>
				<dfn>Note credito</dfn>
				<dl><s:property value="documento.importoTotaleNoteCollegate"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Documenti collegati</dfn>
				<dl><s:property value="totaleImportoDocumentiCollegati"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Note</dfn>
				<dl><s:property value="documento.note"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice PCC</dfn>
				<dl>
					<s:if test="%{documento.codicePCC != null}">
						<s:property value="documento.codicePCC.codice"/>&nbsp;-&nbsp;<s:property value="documento.codicePCC.descrizione"/>
					</s:if>&nbsp;
				</dl>
			</li>
			<li>
				<dfn>Codice Ufficio Destinatario FEL</dfn>
				<dl>
					<s:if test="%{documento.codiceUfficioDestinatario != null}">
						<s:property value="documento.codiceUfficioDestinatario.codice"/>&nbsp;-&nbsp;<s:property value="documento.codiceUfficioDestinatario.descrizione"/>
					</s:if>&nbsp;
				</dl>
			</li>
			<li>
				<dfn>Documento pagato</dfn>
				<dl><s:property value="isDocumentoPagatoIncassato" escapeHtml="false"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Note pagamento</dfn>
				<dl><s:property value="documento.datiFatturaPagataIncassata.notePagamentoIncasso" />&nbsp;</dl>
			</li>
			<li>
				<dfn>Data pagamento</dfn>
				<dl><s:property value="documento.datiFatturaPagataIncassata.dataOperazione" />&nbsp;</dl>
			</li>
		</ul>
	</div>
	<div class="clear"></div>
	<div id="accordionOrdini" class="accordion">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a href="#collapseOrdini" data-parent="#accordionOrdini" data-toggle="collapse" class="accordion-toggle collapsed">
					Ordini<span class="icon"></span>
				</a>
			</div>
			<div class="accordion-body collapse" id="collapseOrdini"></div>
		</div>
	</div>
</fieldset>