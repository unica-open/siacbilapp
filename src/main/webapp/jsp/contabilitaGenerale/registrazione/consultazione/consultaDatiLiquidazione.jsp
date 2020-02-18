<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane"><s:property value="consultazioneHelper.datiCreazioneModifica" /></h4>
<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Liquidazione</p>
			<ul class="htmlelt">
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.descrizioneLiquidazione"/></dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.importoLiquidazione"/></dl>
				</li>
				<li>
					<dfn>Stato</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.statoOperativoLiquidazione"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight-multi">
			<p>Dati Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Impegno</dfn>
					<dl><s:property value="consultazioneHelper.datiImpegno" /></dl>
				</li>
				<li>
					<dfn>Subimpegno</dfn>
					<dl><s:property value="consultazioneHelper.numeroSubImpegno" /></dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.impegno.descrizione"/></dl>
				</li>
				<li>
					<dfn>Mutuo</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.numeroMutuo"/></dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Capitolo</p>
			<ul class="htmlelt">
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseCapitolo" /></dl>
				</li>

				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseStrutturaAmministrativoContabileCapitolo" /></dl>
				</li>
				<li>
					<dfn>Tipo finanziamento</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseTipoFinanziamentoCapitolo" /></dl>
				</li>
			</ul>
		</div>

		<div class="boxOrInRight-multi">
			<p>Dati Soggetto</p>
			<ul class="htmlelt">
				<li>
					<dfn>Codice</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.soggettoLiquidazione.codiceSoggetto"/></dl>
				</li>
				<li>
					<dfn>Denominazione</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.soggettoLiquidazione.denominazione"/></dl>
				</li>
				<li>
					<dfn>Codice Fiscale</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.soggettoLiquidazione.codiceFiscale"/></dl>
				</li>
				<li>
					<dfn>Partita IVA</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.soggettoLiquidazione.partitaIva"/></dl>
				</li>
				<li>
					<dfn>Modalit&agrave; di Pagamento</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.modalitaPagamento.descrizione"/></dl>
				</li>
			</ul>
		</div>
	</div>
</div>

<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Provvedimento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Provvedimento</dfn>
					<dl><s:property value="consultazioneHelper.datiAttoAmministrativo" /></dl>
				</li>
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.attoAmministrativoLiquidazione.tipoAtto.descrizione"/></dl>
				</li>
				<li>
					<dfn>Oggetto</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.attoAmministrativoLiquidazione.oggetto"/></dl>
				</li>
				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiStrutturaAmministrativoContabile" /></dl>
				</li>
				<li>
					<dfn>Stato</dfn>
					<dl><s:property value="consultazioneHelper.liquidazione.attoAmministrativoLiquidazione.statoOperativo"/></dl>
				</li>
			</ul>
		</div>
	</div>
</div>
