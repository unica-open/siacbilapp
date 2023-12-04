<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>
<fieldset class="form-horizontal">
	<h4><s:property value="stringaRiepilogoRichiestaEconomale"/></h4>
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Dati richiedente</p>
			<ul class="htmlelt">
				<li><%--SIAC-2932 --%>
					<dfn>Matricola</dfn>
					<dl>&nbsp;</dl>
				</li>
				<li><%--SIAC-2932 --%>
					<dfn>Unit&agrave; organizzativa</dfn>
					<dl>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Dati richiesta</p>
			<ul class="htmlelt">
				<li>
					<dfn>Descrizione della spesa</dfn>
					<dl><s:property value="richiestaEconomale.descrizioneDellaRichiesta"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Delegato incasso</dfn>
					<dl><s:property value="richiestaEconomale.delegatoAllIncasso"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Note</dfn>
					<dl><s:property value="richiestaEconomale.note"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="descrizioneCapitoloMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data operazione</dfn>
					<dl><s:property value="richiestaEconomale.movimento.dataMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Modalit&agrave; di pagamento</dfn>
					<dl><s:property value="stringaRiepilogoModalitaDiPagamento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Rendiconto n.</dfn>
					<dl><s:property value="richiestaEconomale.numeroRendicontoStampato"/><span> - </span><b>del</b> <s:property value="richiestaEconomale.dataRendicontoStampato"/></dl>
				</li>
				<li>
					<dfn>Importo pagato</dfn>
					<dl><s:property value="importoPagato"/></dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="richiestaEconomale.impegno.annoMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="richiestaEconomale.impegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>SubImpegno</dfn>
					<dl><s:property value="richiestaEconomale.subImpegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="richiestaEconomale.impegno.descrizione"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Dati fattura</p>
			<ul class="htmlelt">
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="documentoSpesa.tipoDocumento.descrizione"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="documentoSpesa.anno"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="documentoSpesa.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Quota</dfn>
					<dl><s:property value="descrizioneQuoteAssociate"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data</dfn>
					<dl><s:property value="documentoSpesa.dataEmissione"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="documentoSpesa.importo"/><s:if test="%{importoSplitReverse > 0}"><span> </span><b>(di cui <s:property value="importoSplitReverse"/> split/reverse)</b></s:if>&nbsp;</dl>
				</li>
				<li>
					<dfn>Codice</dfn>
					<dl><s:property value="documentoSpesa.soggetto.codiceSoggetto"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Denominazione</dfn>
					<dl><s:property value="documentoSpesa.soggetto.denominazione"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Soggetto</dfn>
					<dl><s:property value="denominazioneSoggetto"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Rilevante iva</dfn>
					<dl><s:if test="%{rilevanteIVA}">S&igrave;</s:if><s:else>No</s:else></dl>
				</li>
			</ul>
		</div>
	</div>

</fieldset>