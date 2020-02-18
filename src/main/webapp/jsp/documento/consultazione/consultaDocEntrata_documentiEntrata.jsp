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
		<p>Documenti entrata</p>
		<ul class="htmlelt">
			<li>
				<dfn>Tipo</dfn>
				<dl><s:property value="documento.tipoDocumento.codice"/>&nbsp;-&nbsp;<s:property value="documento.tipoDocumento.descrizione"/>&nbsp;</dl>
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
				<dfn>Data</dfn>
				<dl><s:property value="documento.dataEmissione"/>&nbsp;</dl>
			</li>
			<!-- SIAC 6677 -->
			<li>
				<dfn>Data Operazione</dfn>
				<dl><s:property value="documento.dataOperazione"/>&nbsp;</dl>
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
			<!-- SIAC-6565-CR1215 -->
			<li>
				<dfn>E-mail PEC</dfn>
				<dl><s:property value="documento.soggetto.emailPec" />&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice Destinatario/ IPA</dfn>
				<dl><s:property value="documento.soggetto.codDestinatario" />&nbsp;</dl>
			</li>
			<li>
				<dfn>Debitore multiplo</dfn>
				<dl>
					<s:if test="documento.flagDebitoreMultiplo">S&iacute;</s:if><s:else>No</s:else>
				</dl>
			</li>
			<%-- LOTTO M - Documenti Bozze - --%>
			<%--
			<li>
				<dfn>Attivazione registrazioni contabili</dfn>
				<dl>
					<s:if test="%{indicatoreContabilizzaGENPCC}">S&igrave;</s:if><s:else>No</s:else>
				</dl>
			</li>
			--%>
		</ul>
	</div>
	<div class="boxOrInRight">
		<p>Altri dati</p>
		<ul class="htmlelt">
			<li>
				<dfn>Termine di pagamento</dfn>
				<dl><s:property value="documento.terminePagamento"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data scadenza</dfn>
				<dl><s:property value="documento.dataScadenza"/>&nbsp;</dl>
			</li>
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
			<li>
				<dfn>Codice Avviso Pago PA</dfn>
				<dl><s:property value="documento.codAvvisoPagoPA"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>IUV</dfn>
				<dl><s:property value="documento.iuv"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Importi</dfn>
				<dl>
					<b>Documento</b> <span class="datiIns"><s:property value="documento.importo"/></span>&nbsp;-&nbsp;
					<b>Arrotondamento</b> <span class="datiIns"><s:property value="documento.arrotondamento"/></span>&nbsp;-&nbsp;
					<b>Netto</b> <span class="datiIns"></span><s:property value="netto"/>&nbsp;-&nbsp;
					<b>Note credito</b> <span class="datiIns"><s:property value="documento.importoTotaleNoteCollegate"/></span>&nbsp;-&nbsp;
					<b>Documenti collegati</b> <span class="datiIns"><s:property value="totaleImportoDocumentiCollegati"/></span>
				</dl>
			</li>
			<li>
				<dfn>Descrizione</dfn>
				<dl><s:property value="documento.descrizione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Codice bollo</dfn>
				<dl><s:property value="documento.codiceBollo.codice"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Note</dfn>
				<dl><s:property value="documento.note"/>&nbsp;</dl>
			</li>
		</ul>
	</div>
</fieldset>