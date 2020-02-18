<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion" id="accordionDatiRichiesta">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a data-target="#collapseDatiRichiesta" data-parent="#accordionDatiRichiesta" data-toggle="collapse" class="accordion-toggle">
				<s:property value="denominazioneRichiestaPerRendiconto"/><span class="icon">&nbsp;</span>
			</a>
		</div>
		<div class="accordion-body in collapse" id="collapseDatiRichiesta">
			<div class="accordion-inner">
				<div class="boxOrSpan2">
					<div class="boxOrInLeft">
						<p>Dati richiedente</p>
						<ul class="htmlelt">
							<li>
								<dfn>Matricola</dfn>
								<dl><s:property value="%{rendicontoRichiesta.richiestaEconomale.soggetto.matricola + ' - ' + rendicontoRichiesta.richiestaEconomale.soggetto.denominazione}"/>&nbsp;</dl>
							</li>
							<li>
								<dfn>Unit&agrave; organizzativa</dfn>
								<dl><s:property value="rendicontoRichiesta.richiestaEconomale.strutturaDiAppartenenza"/>&nbsp;</dl>
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
								<dfn>Capitolo</dfn>
								<dl><s:property value="descrizioneCapitoloMovimentoRichiesta"/>&nbsp;</dl>
							</li>
							<li>
								<dfn>Data operazione</dfn>
								<dl><s:property value="rendicontoRichiesta.richiestaEconomale.movimento.dataMovimento"/>&nbsp;</dl>
							</li>
							<li>
								<dfn>Importo anticipato</dfn>
								<dl><s:property value="rendicontoRichiesta.richiestaEconomale.importo"/>&nbsp;</dl>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>