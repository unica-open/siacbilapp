<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div class="accordion-inner">
	<h4>Inserimento: <s:property value="%{subdocumentoEntrata.dataCreazione}"/> (<s:property value="%{subdocumentoEntrata.loginCreazione}"/>) -
		Ultima modifica: <s:property value="%{subdocumentoEntrata.dataModifica}"/> (<s:property value="%{subdocumentoEntrata.loginModifica}"/>&nbsp;)</h4>
	
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Quota</p>
			<ul class="htmlelt">
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="%{subdocumentoEntrata.numero}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="%{subdocumentoEntrata.importo}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data scadenza</dfn>
					<dl><s:property value="%{subdocumentoEntrata.dataScadenza}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Sede</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.sedeSecondariaSoggetto != null}">
							<s:property value="%{subdocumentoEntrata.sedeSecondariaSoggetto.codiceSedeSecondaria}" /> -
							<s:property value="%{subdocumentoEntrata.sedeSecondariaSoggetto.denominazione}" />
						</s:if>
					</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="%{subdocumentoEntrata.descrizione}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Allegato atto</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.elencoDocumenti != null && subdocumentoEntrata.elencoDocumenti.allegatoAtto.attoAmministrativo != null}">
						<s:property value="%{subdocumentoEntrata.elencoDocumenti.allegatoAtto.attoAmministrativo.anno}" /> /
								<s:property value="%{subdocumentoEntrata.elencoDocumenti.allegatoAtto.attoAmministrativo.tipoAtto.codice}" /> /
								<s:property value="%{subdocumentoEntrata.elencoDocumenti.allegatoAtto.attoAmministrativo.numero}" /> 
						</s:if>
					</dl>
				</li>
				<li>
					<dfn>Elenco documenti Allegato</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.elencoDocumenti != null}">
							<s:property value="%{subdocumentoEntrata.elencoDocumenti.anno}" /> /
							<s:property value="%{subdocumentoEntrata.elencoDocumenti.numero}" />
						</s:if>
					</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Accertamento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="%{subdocumentoEntrata.accertamento.annoMovimento}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="%{subdocumentoEntrata.accertamento.numero.toString()}"/>&nbsp;</dl>
				</li>
				<s:if test="%{subdocumentoEntrata.subAccertamento}">
					<li>
						<dfn>Subaccertamento</dfn>
						<dl><s:property value="%{subdocumentoEntrata.subAccertamento.numero.toString()}"/>&nbsp;</dl>
					</li>
				</s:if>
				<li>
					<dfn>Disponibilit&agrave; a incassare</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.subImpegno != null }">
							<s:property value="%{subdocumentoEntrata.subAccertamento.disponibilitaIncassare}" />
						</s:if><s:else>
							<s:property value="%{subdocumentoEntrata.accertamento.disponibilitaIncassare}" />
						</s:else>
						&nbsp;
					</dl>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Provvedimento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="%{subdocumentoEntrata.attoAmministrativo.tipoAtto.codice}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="%{subdocumentoEntrata.attoAmministrativo.anno}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="%{subdocumentoEntrata.attoAmministrativo.numero}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Struttura</dfn>
					<dl><s:property value="%{subdocumentoEntrata.attoAmministrativo.strutturaAmmContabile.codice}"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Iva</p>
			<ul class="htmlelt">
				<li>
					<dfn>Rilevante</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.flagRilevanteIVA}">S&igrave;</s:if><s:else>No</s:else>
					</dl>&nbsp;
				</li>
				<li>
					<dfn>Numero registrazione</dfn>
					<dl><s:property value="%{subdocumentoEntrata.numeroRegistrazioneIVA}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Capitolo rilevante iva</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.accertamento.capitoloEntrataGestione.flagRilevanteIva}">S&iacute;</s:if><s:else>No</s:else>
					</dl>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Provvisorio di Cassa</p>
			<ul class="htmlelt">
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="%{subdocumentoEntrata.provvisorioCassa.numero}"/></dl>
				</li>
				<li>
					<dfn>Data</dfn>
					<dl><s:property value="%{subdocumentoEntrata.provvisorioCassa.dataEmissione}"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Altri dati</p>
			<ul class="htmlelt">
				<li>
					<dfn>Avviso</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.flagAvviso}">S&igrave;</s:if><s:else>No</s:else>&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Ordinativo singolo</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.flagOrdinativoSingolo}">S&igrave;</s:if><s:else>No</s:else>&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Esproprio</dfn>
					<dl>
						<s:if test="%{subdocumentoEntrata.flagEsproprio}">S&igrave;</s:if><s:else>No</s:else>
					</dl>
				</li>
				<li>
					<dfn>Note al tesoriere</dfn>
					<dl><s:property value="%{subdocumentoEntrata.noteTesoriere.codice}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Note</dfn>
					<dl><s:property value="%{subdocumentoEntrata.note}"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Predisposizione</dfn>
					<dl><s:property value="%{subdocumentoEntrata.preDocumentoEntrata.numero}"/> - <s:property value="%{subdocumentoEntrata.preDocumentoEntrata.descrizione}"/></dl>
				</li>
			</ul>
		</div>
	</div>
</div>