<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane">
	Inserimento: 
	<s:property value="primaNota.dataCreazionePrimaNota"/> (<s:property value="primaNota.loginCreazione" />)
	<s:if test="%{primaNota.dataModificaPrimaNota != null}">
		- Ultima modifica: <s:property value="primaNota.dataModificaPrimaNota"/> (<s:property value="primaNota.loginModifica" />)
	</s:if>
</h4>
<div class="boxOrSpan2">
	<div class="boxOrInline">
		<p>Dati Causale</p>
		<ul class="htmlelt">
			<li>
				<dfn>Causale</dfn>
				<dl><s:property value="descrizioneCausale"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Data registrazione</dfn>
				<dl><s:property value="primaNota.dataRegistrazione"/>&nbsp;</dl>
			</li>
			<li>
				<dfn>Descrizione</dfn>
				<dl><s:property value="primaNota.descrizione"/>&nbsp;</dl>
			</li>
		</ul>
	</div>
</div>
<div class="clear"></div>
<br />
<div id="accordionPrimeNoteCollegate" class="accordion">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a href="#divPrimeNoteCollegate" data-parent="#accordionPrimeNoteCollegate" data-toggle="collapse" class="accordion-toggle collapsed">
				Prime note collegate
				<s:if test="%{primaNota.listaPrimaNotaFiglia != null && primaNota.listaPrimaNotaFiglia.size()>0}">
				 	<span>: totale &nbsp;<s:property value="primaNota.listaPrimaNotaFiglia.size()" /></span>
				</s:if>
				<span class="icon">&nbsp;</span>
			</a>
		</div>
		<div class="accordion-body collapse" id="divPrimeNoteCollegate">
			<div class="accordion-inner">
				<table class="table table-hover tab_left" id="tabellaPrimeNoteCollegate">
					<thead>
						<tr>
							<th>Tipo</th>
							<th>Anno</th>
							<th>N.provvisorio</th>
							<th>N.definitivo</th>
							<th>Motivazione</th>
							<th>Stato</th>
						</tr>
					</thead>
					<tbody>
					<s:iterator value="primaNota.listaPrimaNotaFiglia" var="pNotaCollegata">
						<tr>
							<td><s:property value="#pNotaCollegata.tipoCausale.descrizione" /></td>
							<td><s:property value="#pNotaCollegata.bilancio.anno" /></td>
							<td><s:property value="#pNotaCollegata.numero" />
							<td><s:property value="#pNotaCollegata.numeroRegistrazioneLibroGiornale" />
							<td>
								<s:if test='%{#pNotaCollegata.tipoRelazionePrimaNota != null}'>
									<s:property value="#pNotaCollegata.tipoRelazionePrimaNota.descrizione" />
								</s:if>
							</td>
							<td>
								<s:if test='%{#pNotaCollegata.statoOperativoPrimaNota != null}'>
									<s:property value="#pNotaCollegata.statoOperativoPrimaNota.descrizione" />
								</s:if>
							</td>
						</tr>
					</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<br/>

<s:if test="consultazioneDatiFinanziariAbilitata">
	<div id="accordionDatiFinanziari" class="accordion">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a href="#divDatiFinanziari" data-parent="#accordionDatiFinanziari" data-toggle="collapse" class="accordion-toggle collapsed" id="headingAccordionDatiFinanziari">
					Dati finanziari<span class="icon">&nbsp;</span>
				</a>
			</div>
			<div class="accordion-body collapse" id="divDatiFinanziari"></div>
		</div>
	</div>
</s:if>