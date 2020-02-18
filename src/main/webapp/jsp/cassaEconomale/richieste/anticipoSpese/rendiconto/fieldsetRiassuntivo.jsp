<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>
<fieldset class="form-horizontal">
	<h4><s:property value="stringaRiepilogoRichiestaEconomale"/></h4>
	<div class="boxOrSpan2 margin-medium">
		<div class="boxOrInLeft">
			<p>Dati richiedente</p>
			<ul class="htmlelt">
				<li>
					<dfn>Matricola</dfn>
					<dl><s:property value="datiMatricola" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Unit&agrave; organizzativa</dfn>
					<dl><s:property value="rendicontoRichiesta.richiestaEconomale.strutturaDiAppartenenza"/>&nbsp;</dl>
				</li>
			</ul>
			<p>Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="rendicontoRichiesta.richiestaEconomale.impegno.annoMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><si:plainstringproperty value="rendicontoRichiesta.richiestaEconomale.impegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>SubImpegno</dfn>
					<dl><si:plainstringproperty value="rendicontoRichiesta.richiestaEconomale.subImpegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="rendicontoRichiesta.richiestaEconomale.impegno.descrizione"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Dati richiesta</p>
			<ul class="htmlelt">
				<li>
					<dfn>Delegato incasso</dfn>
					<dl><s:property value="rendicontoRichiesta.richiestaEconomale.delegatoAllIncasso"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="rendicontoRichiesta.richiestaEconomale.descrizioneDellaRichiesta"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Note</dfn>
					<dl><s:property value="rendicontoRichiesta.note"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="descrizioneCapitoloMovimentoRichiesta"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data operazione</dfn>
					<dl><s:property value="rendicontoRichiesta.movimento.dataMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Modalit&agrave; di pagamento</dfn>
					<dl><s:property value="stringaRiepilogoModalitaDiPagamento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Stampa Rendiconto Cassa n.</dfn>
					<dl><s:property value="rendicontoRichiesta.numeroRendicontoStampato"/><span> - </span><b>del</b> <s:property value="rendicontoRichiesta.dataRendicontoStampato"/></dl>
				</li>
				<li>
					<dfn>Importo anticipato</dfn>
					<dl><s:property value="importoRichiestaAnticipato"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo restituito</dfn>
					<dl><s:property value="importoDaRestituire"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo integrato</dfn>
					<dl><s:property value="importoDaIntegrare"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
	</div>
	
	<h4>Giustificativi</h4>
	<table class="table table-hover tab_left" id="tabellaGiustificativi">
		<thead>
			<tr>
				<th class="span8">Tipo</th>
				<th class="span2">Anno</th>
				<th class="span2"><abbr title="Numero">N.</abbr> Protocollo</th>
				<th class="span2">Data emissione</th>
				<th class="span2">Numero</th>
				<th class="tab_Right span5">Importo</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rendicontoRichiesta.giustificativi" var="giu">
				<tr>
					<td><s:property value="#giu.tipoGiustificativo.descrizione" /></td>
					<td><s:property value="#giu.annoProtocollo" /></td>
					<td><s:property value="#giu.numeroProtocollo" /></td>
					<td><s:property value="#giu.dataEmissione" /></td>
					<td><s:property value="#giu.numeroGiustificativo" /></td>
					<td class="tab_Right"><s:property value="#giu.importoGiustificativo" /></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="5">Totale</th>
				<th class="tab_Right"><s:property value="totaleImportiGiustificativi"/></th>
			</tr>
		</tfoot>
	</table>
</fieldset>