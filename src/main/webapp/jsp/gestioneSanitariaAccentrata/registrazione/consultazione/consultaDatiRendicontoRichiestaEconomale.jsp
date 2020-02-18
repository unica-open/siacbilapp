<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Richiedente</p>
			<ul class="htmlelt">
				<li>
					<dfn>Matricola</dfn>
					<dl><s:property value="consultazioneHelper.richiestaEconomale.matricola"/></dl>
				</li>
				<li>
					<dfn>Unit&agrave; organizzativa</dfn>
					<dl><s:property value="consultazioneHelper.richiestaEconomale.strutturaDiAppartenenza"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight-multi">
			<p>Dati Richiesta</p>
			<ul class="htmlelt">
				<li>
					<dfn>Descrizione della spesa</dfn>
					<dl><s:property value="consultazioneHelper.richiestaEconomale.descrizioneDellaRichiesta"/></dl>
				</li>
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseCapitolo" /></dl>
				</li>
				<li>
					<dfn>Data Operazione</dfn>
					<dl><s:property value="consultazioneHelper.richiestaEconomale.movimento.dataMovimento"/></dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="consultazioneHelper.richiestaEconomale.importo"/></dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="consultazioneHelper.rendicontoRichiesta.impegno.annoMovimento"/></dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="consultazioneHelper.numeroImpegno"/></dl>
				</li>
				<s:if test="consultazioneHelper.subImpegnoPresente">
					<li>
						<dfn>SubImpegno</dfn>
						<dl><s:property value="consultazioneHelper.numeroSubImpegno"/></dl>
					</li>
				</s:if>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="consultazioneHelper.rendicontoRichiesta.impegno.descrizione"/></dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Rendiconto</p>
			<ul class="htmlelt">
				<li><dfn>Importo integrato</dfn>
					<dl>
						<s:property value="consultazioneHelper.rendicontoRichiesta.importoIntegrato" />
					</dl>
				</li>
				<li><dfn>Importo restituito</dfn>
					<dl>
						 <s:property value="consultazioneHelper.rendicontoRichiesta.importoRestituito" />
					</dl>
				</li>
			</ul>
		</div>
	</div>
</div>
