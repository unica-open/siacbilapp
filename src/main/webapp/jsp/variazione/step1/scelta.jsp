<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>

	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	
	<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">


			<div class="span12 contentPage">
				
				<s:include value="/jsp/include/messaggi.jsp"/>

				<s:form action="esecuzioneStep1InserimentoVariazioni" id="scelta" novalidate="novalidate" cssClass="form-horizontal">
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active">
								<span class="badge">1</span>
								Scegli
								<span class="chevron"></span>
							</li>
							<li data-target="#step2">
								<span class="badge">2</span>
								Definisci
								<span class="chevron"></span>
							</li>
							<li data-target="#step3">
								<span class="badge">3</span>
								Specifica
								<span class="chevron"></span>
							</li>
							<li data-target="#step4">
								<span class="badge">4</span>
								Riepilogo
								<span class="chevron"></span>
							</li>
						</ul>
					</div>

					<div class="step-content">
						<div class="step-pane active" id="step1">
							<div class="control-group">
								<span class="control-label">Seleziona il tipo di variazione:</span>
								<div class="controls">
									<label class="radio">
										<input type="radio" name="scelta.sceltaSt" value="importi" <s:if test="%{scelta.sceltaSt eq 'IMPORTI'}">checked="checked"</s:if>/> 
										Variazioni di importi
										
									</label>
									<label class="radio"> 
									<!-- SIAC-6884 -->
										<input type="radio"	name="scelta.sceltaSt" <s:if test="%{model.isDecentrato}">disabled</s:if> value="codifiche" <s:if test="%{scelta.sceltaSt eq 'CODIFICHE'}">checked="checked"</s:if>/>
										Variazioni di codifiche
									</label>
								</div>
							</div>
							<p>									
								<s:include value="/jsp/include/indietro.jsp" />
								<button type="button" class="btn btn-link reset">annulla</button>
								<s:submit cssClass="btn btn-primary pull-right" value="prosegui"/>
							</p>
						</div>

					</div>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>
