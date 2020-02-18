<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner" >
	<h4>
		Inserimento:
		<s:property value="subdocumentoSpesa.dataCreazione" /> (<s:property value="subdocumentoSpesa.loginCreazione" />&nbsp;)
		- Ultima modifica: <s:property value="subdocumentoSpesa.dataModifica" /> (<s:property value="subdocumentoSpesa.loginModifica" />&nbsp;)
	</h4>
	<div class="boxOrSpan2">

		<div class="boxOrInLeft">
			<p>Quota</p>
			<ul class="htmlelt">
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="subdocumentoSpesa.numero" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="subdocumentoSpesa.importo" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Data scadenza</dfn>
					<dl><s:property value="subdocumentoSpesa.dataScadenza" />&nbsp;</dl>
				</li>
				
				<li>
					<dfn>Data scadenza dopo sospensione</dfn>
					<dl><s:property value="subdocumentoSpesa.dataScadenzaDopoSospensione" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Motivo scadenza siope</dfn>
					<dl><s:property value="motivoScadenzaSiope" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="subdocumentoSpesa.importo" />&nbsp;</dl>
				</li>
				<s:if test="subdocumentoSpesa.pagatoInCEC">
					<li>
						<dfn>&nbsp;</dfn>
						<dl>QUOTA PAGATA IN CASSA ECONOMALE</dl>
					</li>
				</s:if>
				<s:if test="%{subdocumentoSpesa.dataPagamentoCEC != null}">
					<li>
						<dfn>Data pagamento CEC</dfn>
						<dl><s:property value="subdocumentoSpesa.dataPagamentoCEC" />&nbsp;</dl>
					</li>
				</s:if>
				<li>
					<dfn>Data esecuzione pagamento</dfn>
					<dl><s:property value="subdocumentoSpesa.dataEsecuzionePagamento" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="subdocumentoSpesa.descrizione" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Allegato atto</dfn>
					<dl><s:property value="allegatoAttoQuota" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Elenco documenti Allegato</dfn>
					<dl>
						<s:if test="%{subdocumentoSpesa.elencoDocumenti != null}">
							<s:property value="subdocumentoSpesa.elencoDocumenti.anno" /> /
							<s:property value="subdocumentoSpesa.elencoDocumenti.numero" />
						</s:if>
					</dl>
				</li>
			</ul>
		</div>

		<div class="boxOrInRight">
			<p>Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="subdocumentoSpesa.impegno.annoMovimento" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="%{subdocumentoSpesa.impegno.numero.toString()}" />&nbsp;</dl>
				</li>
				<s:if test="%{subdocumentoSpesa.subImpegno != null}">
					<li>
						<dfn>Subimpegno</dfn>
						<dl><s:property value="%{subdocumentoSpesa.subImpegno.numero.toString()}" />&nbsp;</dl>
					</li>
				</s:if>
				<li>
					<dfn>Tipo debito SIOPE</dfn>
					<dl><s:property value="tipoDebitoSiopeImpegno" />&nbsp;</dl>
				</li>
				<li>
					<dfn>CIG</dfn>
					<dl><s:property value="subdocumentoSpesa.cig" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Motivo assenza CIG</dfn>
					<dl><s:property value="motivoAssenzaCigSiope" />&nbsp;</dl>
				</li>
				<li>
					<dfn>CUP</dfn>
					<dl><s:property value="subdocumentoSpesa.cup" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Nr. Mutuo</dfn>
					<dl><s:property value="subdocumentoSpesa.voceMutuo.numeroMutuo" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Disponibilit&agrave; di cassa</dfn>
					<dl>
						<s:if test="%{subdocumentoSpesa.subImpegno != null}">
							<s:property value="subdocumentoSpesa.subImpegno.disponibilitaPagare" />
						</s:if><s:else>
							<s:property value="subdocumentoSpesa.impegno.disponibilitaPagare" />
						</s:else>
						&nbsp;
					</dl>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Altri dati</p>
			<ul class="htmlelt">
				<li>
					<dfn>Avviso</dfn>
					<dl><s:property value="subdocumentoSpesa.tipoAvviso.codice" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Ordinativo singolo</dfn>
					<dl>
						<s:if test="subdocumentoSpesa.flagOrdinativoSingolo">S&igrave;</s:if>
						<s:else>No</s:else>
						&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Esproprio</dfn>
					<dl>
						<s:if test="subdocumentoSpesa.flagEsproprio">S&igrave;</s:if>
						<s:else>No</s:else>
						&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Note al tesoriere</dfn>
					<dl><s:property value="subdocumentoSpesa.noteTesoriere.codice" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Commissioni</dfn>
					<dl><s:property value="subdocumentoSpesa.commissioniDocumento" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Causale ordinativo</dfn>
					<dl><s:property value="subdocumentoSpesa.causaleOrdinativo" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Note</dfn>
					<dl><s:property value="subdocumentoSpesa.note" />&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Dati Iva Split / Reverse</p>
			<ul class="htmlelt">
				<li>
					<dfn>Tipo Iva Split/Reverse/Esente</dfn>
					<dl>
						<s:if test="%{subdocumentoSpesa.tipoIvaSplitReverse != null}">
							<s:property value="subdocumentoSpesa.tipoIvaSplitReverse.codice"/> - <s:property value="subdocumentoSpesa.tipoIvaSplitReverse.descrizione"/>
						</s:if>&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="subdocumentoSpesa.importoSplitReverseNotNull" />&nbsp;</dl>
				</li>
			</ul>
		</div>
	</div>

	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Modalit&agrave; di pagamento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Modalit&agrave;</dfn>
					<dl><s:property value="subdocumentoSpesa.modalitaPagamentoSoggetto.descrizioneInfo.descrizioneArricchita"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Sede</dfn>
					<dl>
						<s:if test="%{subdocumentoSpesa.sedeSecondariaSoggetto != null}">
							<s:property value="subdocumentoSpesa.sedeSecondariaSoggetto.codiceSedeSecondaria" /> -
							<s:property value="subdocumentoSpesa.sedeSecondariaSoggetto.denominazione" />
						</s:if>
						&nbsp;
					</dl>
				</li>
			</ul>
			<p>Iva</p>
			<ul class="htmlelt">
				<li>
					<dfn>Rilevante</dfn>
					<dl>
						<s:if test="subdocumentoSpesa.flagRilevanteIVA">S&igrave;</s:if>
						<s:else>No</s:else>
						&nbsp;
					</dl>
				</li>
				<li>
					<dfn>Numero registrazione</dfn>
					<dl><s:property value="subdocumentoSpesa.numeroRegistrazioneIVA" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Capitolo rilevante iva</dfn>
					<dl>
						<s:if test="subdocumentoSpesa.impegno.capitoloUscitaGestione.flagRilevanteIva">S&igrave;</s:if>
						<s:else>No</s:else>&nbsp;
					</dl>
				</li>
			</ul>
		</div>

		<div class="boxOrInRight">
			<p>Provvedimento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="subdocumentoSpesa.attoAmministrativo.tipoAtto.codice" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="subdocumentoSpesa.attoAmministrativo.anno" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="subdocumentoSpesa.attoAmministrativo.numero" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Struttura</dfn>
					<dl><s:property value="subdocumentoSpesa.attoAmministrativo.strutturaAmmContabile.codice" />&nbsp;</dl>
				</li>
			</ul>

			<p>Provvisorio di Cassa</p>
			<ul class="htmlelt">
				<li>
					<dfn>Numero</dfn>
					<dl><s:property value="subdocumentoSpesa.provvisorioCassa.numero"/></dl>
				</li>
				<li>
					<dfn>Data</dfn>
					<dl><s:property value="subdocumentoSpesa.provvisorioCassa.dataEmissione"/></dl>
				</li>
			</ul>
		</div>
	</div>

	<div class="boxOrSpan2">
		<h4>Dati sospensione quota</h4>
		<table class="table table-hover tab_left table-show-head" data-table-sospensione>
			<thead class="tablehead-show">
				<tr>
					<th>Data</th>
					<th>Causale</th>
					<th>Data riattivazione</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="subdocumentoSpesa.sospensioni" var="dsq">
					<tr>
						<td><s:property value="#dsq.dataSospensione"/></td>
						<td><s:property value="#dsq.causaleSospensione"/></td>
						<td><s:property value="#dsq.dataRiattivazione"/></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table class=""></table>
	</div>
			
	 <div class="boxOrInline">	 
	 	<button type="button" class="btn btn-secondary visualizzaComunicazioniPCC" data-target="#accordionComunicazioniPCCQuota<s:property value="idWorkaround"/><s:property value="subdocIndex"/>"
			data-uid='<s:property value="subdocumentoSpesa.uid" />' data-toggle="collapse" data-parent="quota<s:property value="idWorkaround"/><s:property value="subdocIndex"/>">
			Comunicazioni a PCC			
		</button>		
		<div id="accordionComunicazioniPCCQuota<s:property value="idWorkaround"/><s:property value="subdocIndex"/>" class="collapse">
		</div>
	</div>
</div>