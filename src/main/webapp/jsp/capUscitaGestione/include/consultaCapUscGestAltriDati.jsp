<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%-- serve affinche' non rimanga la classe boxOr2, che rende sbagliata la formattazione --%>
<h4>&nbsp;</h4>
<div class="accordion" id="accordion2" style="margin-top:20px; margin-bottom:20px;">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAltriDati" data-parent="#accordion2" href="#collapseOne">
				Altri dati<span class="icon">&nbsp;</span>
			</a>
		</div>
		<div id="collapseOne" class="accordion-body collapse in">
			<div class="accordion-inner">
				<ul class="htmlelt">
					<li>
						<dfn>Ex Anno / Capitolo / Articolo<s:if test="gestioneUEB"> / <abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr></s:if></dfn>
						<dl>
							<s:if test="%{capitolo.exCapitolo != null && capitolo.exArticolo != null}">
								<s:property value="capitolo.exAnnoCapitolo"/> / <s:property value="capitolo.exCapitolo"/> / <s:property value="capitolo.exArticolo"/> <s:if test="gestioneUEB"> / <s:property value="capitolo.exUEB"/></s:if>
							</s:if>
						</dl>
					</li>
					<li>
						<dfn>Tipo Finanziamento</dfn>
						<dl><s:property value="tipoFinanziamento.codice"/> <s:property value="tipoFinanziamento.descrizione"/></dl>
					</li>
					<li>
						<dfn>Rilevante IVA</dfn>
						<dl><s:if test="capitolo.flagRilevanteIva">S&iacute;</s:if><s:else>No</s:else></dl>
					</li>
					<li>
						<dfn>Funzioni delegate regione</dfn>
						<dl><s:if test="capitolo.funzDelegateRegione">S&iacute;</s:if><s:else>No</s:else></dl>
					</li>
					<li>
						<dfn>Tipo fondo</dfn>
						<dl><s:property value="tipoFondo.codice"/> <s:property value="tipoFondo.descrizione"/></dl>
					</li>
					<li>
						<dfn>Ricorrente</dfn>
						<dl><s:property value="ricorrenteSpesa.descrizione"/></dl>
					</li>
					<li>
						<dfn>Codifica identificativo del perimetro sanitario</dfn>
						<dl><s:property value="perimetroSanitarioSpesa.codice"/> <s:property value="perimetroSanitarioSpesa.descrizione"/></dl>
					</li>
					<li>
						<dfn>Codifica transazione UE</dfn>
						<dl><s:property value="transazioneUnioneEuropeaSpesa.codice"/> <s:property value="transazioneUnioneEuropeaSpesa.descrizione"/></dl>
					</li>
					<li>
						<dfn>Codifica politiche regionali unitarie</dfn>
						<dl><s:property value="politicheRegionaliUnitarie.codice"/> <s:property value="politicheRegionaliUnitarie.descrizione"/></dl>
					</li>
					<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
						<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null}">
							<li>
								<dfn><s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/></dfn>
								<dl><s:property value="%{#attr['classificatoreGenerico' + #idx + '.codice']}"/> <s:property value="%{#attr['classificatoreGenerico' + #idx + '.descrizione']}"/></dl>
							</li>
						</s:if>
					</s:iterator>
					<li>
						<dfn>Note</dfn>
						<dl><s:property value="capitolo.note"/></dl>
					</li>
				</ul>
			</div>
		</div>										
	</div>	
</div>
								