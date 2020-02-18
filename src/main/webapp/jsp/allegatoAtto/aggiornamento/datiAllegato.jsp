<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form id="formAggiornamentoAllegatoAtto" cssClass="form-horizontal" novalidate="novalidate" action="aggiornaAllegatoAtto_effettuaAggiornamento" method="post">
	<s:hidden id="HIDDEN_uidAllegatoAtto" name="allegatoAtto.uid" />
	<fieldset class="form-horizontal" id="fieldsetDatiAllegatoAtto">
		<h4 class="step-pane">
			Anno: <span class="datiIns"><s:property value="attoAmministrativo.anno"/></span> -
			Numero: <span class="datiIns"><s:property value="attoAmministrativo.numero"/></span> - 
			Struttura: <span class="datiIns"><s:property value="attoAmministrativo.strutturaAmmContabile.codice"/></span>
		</h4>
		<div class="control-group">
			<label class="control-label" for="causaleAllegatoAtto">Causale *</label>
			<div class="controls">
				<s:textfield id="causaleAllegatoAtto" name="allegatoAtto.causale" maxlength="500" cssClass="span9" required="true" readonly="!daCompletare" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="dataScadenzaAllegatoAtto">Data scadenza</label>
			<div class="controls">
				<s:textfield id="dataScadenzaAllegatoAtto" name="allegatoAtto.dataScadenza" cssClass="span2 datepicker" maxlength="10"
					readonly="!daCompletareOCompletato" />
				<span class="alRight">
					<label for="flagRitenuteAllegatoAtto" class="radio inline">Contiene ritenute</label>
				</span>
				<s:checkbox id="flagRitenuteAllegatoAtto" name="allegatoAtto.flagRitenute" disabled="flagRitenuteNonAggiornabile" />
				<s:if test="flagRitenuteNonAggiornabile">
					<s:hidden name="allegatoAtto.flagRitenute" />
				</s:if>
			</div>
		</div>
		<div id="accordionAltriDati" class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#divAltriDati" data-parent="#accordionAltriDati" data-toggle="collapse" class="accordion-toggle collapsed">
						Altri dati<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse" id="divAltriDati">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<br>
							<div class="control-group">
								<label class="control-label" for="responsabileContabileAllegatoAtto">Responsabile contabile</label>
								<div class="controls">
									<s:textfield id="responsabileContabileAllegatoAtto" name="allegatoAtto.responsabileContabile"
										cssClass="span6" maxlength="500" readonly="!daCompletareOCompletato" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="annotazioniAllegatoAtto">Annotazioni</label>
								<div class="controls">
									<s:textarea id="annotazioniAllegatoAtto" name="allegatoAtto.annotazioni" cssClass="span9"
										cols="15" rows="2" maxlength="500"></s:textarea>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="responsabileAmministrativoAllegatoAtto">Responsabile amministrativo</label>
								<div class="controls">
									<s:textfield id="responsabileAmministrativoAllegatoAtto" name="allegatoAtto.responsabileAmministrativo"
										cssClass="span6" maxlength="500" readonly="!daCompletare" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="praticaAllegatoAtto">Pratica numero</label>
								<div class="controls">
									<s:textfield id="praticaAllegatoAtto" name="allegatoAtto.pratica" cssClass="span6" maxlength="500" readonly="!daCompletare" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="altriAllegatiAllegatoAtto">Altri allegati</label>
								<div class="controls">
									<s:textfield id="altriAllegatiAllegatoAtto" name="allegatoAtto.altriAllegati"
										cssClass="span6" maxlength="500" readonly="!daCompletareOCompletato" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="noteAllegatoAtto">Note in Allegato</label>
								<div class="controls">
									<s:textarea id="noteAllegatoAtto" name="allegatoAtto.note" cssClass="span9"
										rows="3" cols="15" readonly="!daCompletareOCompletato"></s:textarea>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="datiSensibiliAllegatoAtto">Contiene dati sensibili</label>
								<div class="controls">
									<s:if test="%{daCompletareOCompletato}">
										<s:checkbox id="datiSensibiliAllegatoAtto" name="allegatoAtto.datiSensibili" />
										<s:hidden name="allegatoAtto.datiSensibili" />
									</s:if><s:else>
										<s:checkbox id="datiSensibiliAllegatoAtto" name="allegatoAtto.datiSensibili" disabled="true" />
									</s:else>
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
		<div class="Border_line"></div>
		<p>
			<s:include value="/jsp/include/indietro.jsp" />
			<s:if test="stampaAbilitato">
				<button type="button" class="btn btn-secondary" data-submit-url="aggiornaAllegatoAtto_stampa.do">stampa</button>
			</s:if>
			<s:if test="invioFluxAbilitato">
				<button type="button" class="btn btn-secondary" data-submit-url="aggiornaAllegatoAtto_invio.do">invio</button>
			</s:if>
			<s:if test="sospendiTuttoAbilitato">
				<button type="button" class="btn btn-secondary" id="sospendiTuttoAllegatoAtto">sospendi tutto</button>
			</s:if>
			<s:submit cssClass="btn btn-primary pull-right" value="salva dati allegato" />
		</p>
	</fieldset>
</s:form>