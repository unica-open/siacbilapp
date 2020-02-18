<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion" id="accordionMovFin">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAltriDati" data-parent="#accordionMovFin" href="#collapseMovFin">
				Movimento Finanziario<span class="icon">&nbsp;</span>
			</a>
		</div>
		<div id="collapseMovFin" class="accordion-body collapse">
			<div class="accordion-inner">
			
				<fieldset class="form-horizontal" data-overlay>
					<div class="control-group">
						<label class="control-label">&nbsp;</label>
						<div class="controls">
							<label class="radio inline">
								<input type="radio" value="Impegno" name="tipoMovimento" disabled <s:if test='%{"Impegno".equals(tipoMovimento)}'>checked</s:if>>Impegno
							</label>
							<span class="alLeft">
								<label class="radio inline">
									<input type="radio" value="Accertamento" name="tipoMovimento" disabled <s:if test='%{"Accertamento".equals(tipoMovimento)}'>checked</s:if>>Accertamento
								</label>
							</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="annoMovimento">Anno</label>
						<div class="controls">
							<s:textfield id="annoMovimento" name="annoMovimentoGestione" disabled="true" required="true" cssClass="span1 soloNumeri" />
							<span class="al">
								<label class="radio inline" for="numeroMovimentoGestione">Numero</label>
							</span>
							<s:textfield id="numeroMovimentoGestione" name="numeroMovimentoGestione" disabled="true" required="true" cssClass="span2 soloNumeri" />
							<span class="al">
								<label class="radio inline" for="numeroSubmovimentoGestione">Sub</label>
							</span>
							<s:textfield id="numeroSubmovimentoGestione" name="numeroSubmovimentoGestione" disabled="true" required="true" cssClass="span2 soloNumeri" />
							<span class="al">
								<label class="radio inline" for="importoAttualeMovimentoGestione">Importo attuale</label>
							</span>
							<s:textfield name="importoAttualeMovimentoGestione" disabled="true" cssClass="span4 soloNumeri text-right" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="annoMovimento">Descrizione</label>
						<div class="controls">
							<s:textarea class="span12" name="descrizioneMovimentoGestione" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="annoMovimento">Soggetto</label>
						<div class="controls">
							<s:textfield class="span12" name="soggettoMovimentoGestione" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="annoMovimento">Capitolo</label>
						<div class="controls">
							<s:textarea class="span12" name="capitoloMovimentoGestione" />
						</div>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
</div>